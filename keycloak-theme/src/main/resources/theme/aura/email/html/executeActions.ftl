<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <meta name="color-scheme" content="light dark">
    <meta name="supported-color-schemes" content="light dark">
    <title>Welcome to Aura</title>
</head>
<body style="margin:0;padding:0;background:#f5f5f7;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif;color:#1d1d1f;-webkit-font-smoothing:antialiased;">

<div style="display:none;max-height:0;overflow:hidden;mso-hide:all;font-size:1px;line-height:1px;color:#f5f5f7;">
    Finish setting up your Aura account.
</div>

<table role="presentation" width="100%" cellpadding="0" cellspacing="0" border="0" style="background:#f5f5f7;">
    <tr>
        <td align="center" style="padding:40px 16px;">

            <table role="presentation" width="560" cellpadding="0" cellspacing="0" border="0"
                   style="background:#ffffff;border-radius:16px;max-width:100%;box-shadow:0 1px 3px rgba(0,0,0,0.04),0 8px 24px rgba(0,0,0,0.06);overflow:hidden;">

                <!-- Logo -->
                <tr>
                    <td align="center" style="padding:32px 32px 8px 32px;">
                        <!-- IMAGE: replace with hosted logo @ 240x64 retina -->
                        <!--
                        <img src="https://cdn.aura.example.com/email/logo.png"
                             alt="Aura" width="96" height="32"
                             style="display:block;border:0;outline:none;text-decoration:none;">
                        -->
                        <span style="font-size:22px;font-weight:700;letter-spacing:-0.01em;color:#1d1d1f;">Aura</span>
                    </td>
                </tr>

                <!-- Hero icon -->
                <tr>
                    <td align="center" style="padding:24px 32px 8px 32px;">
                        <!-- IMAGE: welcome hero illustration @ 480x240 retina -->
                        <!--
                        <img src="https://cdn.aura.example.com/email/hero-welcome.png"
                             alt="" width="240" height="120"
                             style="display:block;border:0;outline:none;text-decoration:none;max-width:100%;height:auto;">
                        -->
                        <div style="display:inline-block;width:72px;height:72px;line-height:72px;border-radius:50%;background:#f0f0f2;font-size:32px;text-align:center;color:#1d1d1f;">&#128075;</div>
                    </td>
                </tr>

                <!-- Headline -->
                <tr>
                    <td align="center" style="padding:16px 32px 8px 32px;">
                        <h1 style="margin:0;font-size:26px;line-height:1.2;font-weight:700;letter-spacing:-0.02em;color:#1d1d1f;">
                            Welcome to Aura
                        </h1>
                    </td>
                </tr>

                <!-- Body -->
                <tr>
                    <td style="padding:16px 40px 8px 40px;font-size:15px;line-height:1.6;color:#3a3a3c;text-align:center;">
                        <p style="margin:0 0 8px 0;">
                            Hi ${user.firstName!},
                        </p>
                        <p style="margin:0;">
                            An account has been created for you. Tap the button below to verify your email and set your password &mdash; both are required to sign in.
                        </p>
                    </td>
                </tr>

                <!-- CTA -->
                <tr>
                    <td align="center" style="padding:24px 32px 8px 32px;">
                        <table role="presentation" cellpadding="0" cellspacing="0" border="0">
                            <tr>
                                <td align="center" style="border-radius:10px;background:#1d1d1f;">
                                    <a href="${link}"
                                       style="display:inline-block;padding:14px 32px;font-size:15px;font-weight:600;color:#ffffff;text-decoration:none;border-radius:10px;font-family:inherit;">
                                        Get started
                                    </a>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

                <!-- Expiry note -->
                <tr>
                    <td align="center" style="padding:8px 40px 24px 40px;font-size:13px;color:#86868b;line-height:1.5;">
                        This link expires in ${linkExpirationFormatter(linkExpiration)}.
                    </td>
                </tr>

                <!-- Fallback link -->
                <tr>
                    <td style="padding:0 40px 32px 40px;">
                        <div style="padding:16px;background:#f7f7f8;border-radius:10px;font-size:12px;line-height:1.55;color:#6e6e73;">
                            <div style="margin-bottom:6px;font-weight:600;color:#3a3a3c;">Button not working?</div>
                            Paste this link into your browser:<br>
                            <a href="${link}" style="color:#1d1d1f;word-break:break-all;text-decoration:underline;">${link}</a>
                        </div>
                    </td>
                </tr>

                <!-- Reassurance -->
                <tr>
                    <td style="padding:0 40px 32px 40px;border-top:1px solid #ececec;">
                        <p style="margin:24px 0 0 0;font-size:13px;line-height:1.55;color:#86868b;text-align:center;">
                            Didn't expect this email? You can safely ignore it.
                        </p>
                    </td>
                </tr>
            </table>

            <!-- Outer footer -->
            <table role="presentation" width="560" cellpadding="0" cellspacing="0" border="0" style="max-width:100%;">
                <tr>
                    <td align="center" style="padding:24px 32px 8px 32px;font-size:12px;line-height:1.6;color:#86868b;">
                        &copy; ${.now?string("yyyy")} Aura. All rights reserved.
                    </td>
                </tr>
            </table>

        </td>
    </tr>
</table>
</body>
</html>
