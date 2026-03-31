---
name: jira-ship-feature
description: Link current PR to its Jira ticket, add a comment with the PR URL, and transition to Review or Done. Use after opening a PR or merging a feature branch.
disable-model-invocation: false
allowed-tools: Bash, Read
---

# Jira Ship Feature

Close the loop on a Jira ticket — link the PR, add a summary comment, and move the ticket to Review or Done.

## Prerequisites

Jira credentials in `.claude/.env` at the project root:

```
JIRA_EMAIL=user@example.com
JIRA_API_TOKEN=your-api-token
JIRA_BASE_URL=https://yourcompany.atlassian.net
```

## Inputs

`$ARGUMENTS` — optional overrides:

- A ticket ID (e.g., `REP-123`) if the branch name doesn't contain one
- `--done` flag to transition to Done instead of Review

If no arguments are given, detect the ticket ID from the current branch name.

## Execution

### Step 1: Load credentials and detect ticket ID

```bash
source .claude/.env
git branch --show-current
```

Extract the ticket ID from the branch name. Expected format: `feat/REP-123-description` → `REP-123`.

If the branch name doesn't match the `REP-\d+` pattern, check `$ARGUMENTS`. If neither has it, ask the user.

### Step 2: Fetch ticket details

```bash
curl -s -u "$JIRA_EMAIL:$JIRA_API_TOKEN" \
  "$JIRA_BASE_URL/rest/api/3/issue/{ticketKey}"
```

Confirm the ticket exists and note its current status.

### Step 3: Get PR URL

```bash
gh pr view --json url -q .url
```

If no PR exists for the current branch, inform the user and ask if they want to continue without a PR link.

### Step 4: Build a summary from recent commits

```bash
git log --oneline main..HEAD
```

Use this to write a short summary of what was done.

### Step 5: Add comment to Jira

Post a comment with the PR URL, branch name, and commit summary:

```bash
curl -s -X POST -u "$JIRA_EMAIL:$JIRA_API_TOKEN" \
  -H "Content-Type: application/json" \
  "$JIRA_BASE_URL/rest/api/3/issue/{ticketKey}/comment" \
  -d '{
    "body": {
      "type": "doc",
      "version": 1,
      "content": [
        {
          "type": "paragraph",
          "content": [{"type": "text", "text": "PR: <url>\nBranch: <branch>\n\nSummary:\n<commit summary>"}]
        }
      ]
    }
  }'
```

### Step 6: Transition the ticket

Fetch available transitions:

```bash
curl -s -u "$JIRA_EMAIL:$JIRA_API_TOKEN" \
  "$JIRA_BASE_URL/rest/api/3/issue/{ticketKey}/transitions"
```

- Default: transition to **Review**
- If `--done` was passed or the user indicates merging: transition to **Done**

Execute the transition. If the ticket is already in the target status, skip and note it.

### Step 7: Confirm

Present:

- **Ticket**: key + summary
- **Status**: transitioned from X → Review/Done
- **Comment**: added with PR link
- **PR**: URL

## When to Use

- After opening a pull request
- After merging a feature branch
- When wrapping up work on a ticket

## When NOT to Use

- Starting work on a ticket — use `/jira-start-ticket` instead
- Creating new tickets
- Sprint planning or bulk ticket operations
