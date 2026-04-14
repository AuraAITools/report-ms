---
name: jira-start-ticket
description: Fetch a Jira ticket, transition it to In Progress, create a feature branch, and display acceptance criteria. Use when starting work on a ticket or picking one up from the sprint board.
disable-model-invocation: false
allowed-tools: Bash, Read
---

# Jira Start Ticket

Kickstart work on a Jira ticket — fetch details, move it to In Progress, create a branch, and surface the acceptance criteria so you know what "done" looks like.

## Prerequisites

Jira credentials in `.claude/.env` at the project root:

```
JIRA_EMAIL=user@example.com
JIRA_API_TOKEN=your-api-token
JIRA_BASE_URL=https://yourcompany.atlassian.net
```

## Inputs

`$ARGUMENTS` — a Jira ticket ID (e.g., `REP-123`).

If no arguments are given, ask the user for the ticket ID.

## Execution

### Step 1: Load credentials

```bash
source .claude/.env
```

Verify the variables are set. If any are missing, tell the user which ones and point them to `.claude/.env`.

### Step 2: Fetch the ticket

```bash
curl -s -u "$JIRA_EMAIL:$JIRA_API_TOKEN" \
  "$JIRA_BASE_URL/rest/api/3/issue/{ticketKey}" | python3 -c "
import sys, json
d = json.load(sys.stdin)
f = d['fields']
print(f'Summary: {f[\"summary\"]}')
print(f'Status: {f[\"status\"][\"name\"]}')
print(f'Priority: {f.get(\"priority\", {}).get(\"name\", \"None\")}')
print(f'Assignee: {(f.get(\"assignee\") or {}).get(\"displayName\", \"Unassigned\")}')
print(f'---')
# Description is ADF — extract text nodes
def extract_text(node):
    if isinstance(node, dict):
        if node.get('type') == 'text': return node.get('text', '')
        return ' '.join(extract_text(c) for c in node.get('content', []))
    if isinstance(node, list): return ' '.join(extract_text(i) for i in node)
    return ''
desc = f.get('description') or {}
print(extract_text(desc))
"
```

If the ticket does not exist (404), inform the user and stop.

### Step 3: Transition to In Progress

First, get available transitions:

```bash
curl -s -u "$JIRA_EMAIL:$JIRA_API_TOKEN" \
  "$JIRA_BASE_URL/rest/api/3/issue/{ticketKey}/transitions"
```

Find the transition that targets "In Progress" (or similar — names vary by project). Execute it:

```bash
curl -s -X POST -u "$JIRA_EMAIL:$JIRA_API_TOKEN" \
  -H "Content-Type: application/json" \
  "$JIRA_BASE_URL/rest/api/3/issue/{ticketKey}/transitions" \
  -d '{"transition": {"id": "TRANSITION_ID"}}'
```

If the ticket is already In Progress, skip and note it.

### Step 4: Create feature branch

Generate a branch name from the ticket:

- Format: `feat/<TICKET-ID>-<slugified-summary>`
- Slug: lowercase, hyphens, max 50 chars, no trailing hyphens
- Example: `feat/REP-123-fix-token-refresh-timing`

```bash
git checkout -b feat/REP-123-<slug>
```

If the branch already exists, check it out instead.

### Step 5: Present summary

Display:

- **Ticket**: key, summary, priority
- **Status**: transitioned from X → In Progress (or already In Progress)
- **Branch**: the created/checked-out branch name
- **Description**: full ticket description so the developer has context

## When to Use

- Starting work on a new Jira ticket
- Picking up a ticket from the sprint board
- Resuming work on an assigned ticket

## When NOT to Use

- Creating new tickets — use the Jira REST API directly
- Bulk operations across multiple tickets
- Wrapping up work — use `/jira-ship-feature` instead
