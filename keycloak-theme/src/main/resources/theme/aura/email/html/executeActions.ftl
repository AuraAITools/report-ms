<html>
<body style="margin:0;padding:0;background:#f5f5f7;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;color:#1d1d1f;">
<table role="presentation" width="100%" cellpadding="0" cellspacing="0" style="background:#f5f5f7;padding:32px 0;">
    <tr>
        <td align="center">
            <table role="presentation" width="560" cellpadding="0" cellspacing="0"
                   style="background:#ffffff;border-radius:12px;padding:32px;max-width:90%;">
                <tr>
                    <td style="padding-bottom:24px;border-bottom:1px solid #ececec;">
                        <span style="font-size:20px;font-weight:600;color:#1d1d1f;">Aura</span>
                    </td>
                </tr>
                <tr>
                    <td style="padding:24px 0;font-size:15px;line-height:1.55;color:#1d1d1f;">
                        <p style="margin:0 0 16px 0;font-size:18px;font-weight:600;">
                            Welcome to Aura, ${user.firstName!}.
                        </p>
                        <p style="margin:0 0 16px 0;">
                            An account has been created for you. Tap the button below to
                            verify your email and set your password — both are required to
                            sign in.
                        </p>
                        <p style="margin:0 0 16px 0;">
                            <a href="${link}"
                               style="display:inline-block;padding:12px 24px;background:#1d1d1f;color:#ffffff;text-decoration:none;border-radius:8px;font-weight:500;">
                                Get started
                            </a>
                        </p>
                        <p style="margin:24px 0 0 0;font-size:13px;color:#86868b;">
                            This link is valid for ${linkExpirationFormatter(linkExpiration)}.
                            If the button doesn't work, copy this URL into your browser:
                            <br>
                            <a href="${link}" style="color:#86868b;word-break:break-all;">${link}</a>
                        </p>
                    </td>
                </tr>
                <tr>
                    <td style="padding-top:24px;border-top:1px solid #ececec;font-size:12px;color:#86868b;line-height:1.55;">
                        If you didn't expect this, you can safely ignore this email.
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
</body>
</html>
