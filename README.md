# Demo of a server that provides an OAuth2 authorization code grant flow

### Generate a new key pair

```bash
keytool -genkeypair -alias oauth2 -keyalg RSA -keysize 2048 -keystore oauth2.jks -validity 360
```