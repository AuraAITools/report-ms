# Aura Keycloak email theme

Custom branded email theme for Keycloak's `VERIFY_EMAIL`, `UPDATE_PASSWORD`,
and `RESET_PASSWORD` flows. Inherits from Keycloak's `base` theme.

## Build

```bash
mvn -pl keycloak-theme -am clean install
```

The resulting JAR (`keycloak-theme-1.0.0.jar`) bundles
`theme/aura/email/{html,text,messages,theme.properties}` resources.

## Deploy to Keycloak

1. Drop the JAR into Keycloak's `providers/` directory:

   ```bash
   cp keycloak-theme/target/keycloak-theme-1.0.0.jar /opt/keycloak/providers/
   ```

2. Build the optimized Keycloak server (only needed once per provider change):

   ```bash
   /opt/keycloak/bin/kc.sh build
   ```

3. Restart Keycloak.

4. In the realm console:
    - Realm settings → Themes → **Email theme** → `aura`.
    - Realm settings → Email → set the SMTP host, port, credentials, and a
      verified `From` address (e.g. `no-reply@aura.com`). Use the same SMTP
      relay (AWS SES) that `report-ms` uses for app-driven emails.

## Templates

| File                                                          | Used for                                                                         |
|---------------------------------------------------------------|----------------------------------------------------------------------------------|
| `html/email-verification.ftl` + `text/email-verification.ftl` | `VERIFY_EMAIL` action sent in isolation                                          |
| `html/executeActions.ftl` + `text/executeActions.ftl`         | Bundled actions (e.g. `VERIFY_EMAIL` + `UPDATE_PASSWORD` after account creation) |
| `html/password-reset.ftl` + `text/password-reset.ftl`         | `RESET_PASSWORD` self-service flow                                               |

`messages/messages_en.properties` overrides Keycloak's default subject lines.

## Variables

Standard Keycloak email-template variables:

- `${user.firstName!}` — recipient first name (with empty fallback)
- `${link}` — the action URL (Keycloak hosted page)
- `${linkExpirationFormatter(linkExpiration)}` — human-friendly expiry text
- `${realmName}` — realm display name

The link points to Keycloak's hosted UI. After completing the action the
user is redirected to the universal link configured in
`aura.email.action-redirect-uri` (set in `report-ms`'s `application.yaml`).
