# In-memory directory server

This directory contains resources to construct an in-memory directory server
for use in tests.

* `test-ldap.keystore` contains a single `PrivateKeyEntry`
    * created by `keytool -genkey -keyalg RSA -keystore test-ldap.keystore -storepass changeit -validity 3600 -keysize 2048 -dname CN=localhost,DC=shibboleth,DC=net`
    * the keystore password is `changeit`
    * the self-signed certificate is for `CN=localhost, DC=shibboleth, DC=net`
    * the certificate's expiration date is set 10 years after creation
* `test-ldap.key` is the 2018-bit RSA public key
    * extract certificate: `keytool -keystore test-ldap.keystore -storepass changeit -exportcert -file temp.crt`
    * extract key: `openssl x509 -inform der -in temp.crt -noout -pubkey >test-ldap.key`
    * display using `openssl rsa -pubin -noout -text -in test-ldap.key`
