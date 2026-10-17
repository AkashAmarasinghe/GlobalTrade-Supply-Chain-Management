#!/usr/bin/env python3
"""Generate an optional local-only SQL file with PBKDF2-HMAC-SHA256 login accounts.
Run AFTER importing database/globaltrade_db.sql. Never commit the output.
"""
import base64
import getpass
import hashlib
import pathlib

ACCOUNTS = [
    ('admin', 'ADMIN', 'GlobalTrade Administrator'),
    ('logistics', 'LOGISTICS_COORDINATOR', 'Logistics Coordinator'),
    ('warehouse', 'WAREHOUSE_MANAGER', 'Warehouse Manager'),
    ('customs', 'CUSTOMS_AGENT', 'Customs Agent'),
    ('vendor', 'VENDOR', 'Vendor Representative'),
    ('customer', 'CUSTOMER', 'Customer Portal User'),
]

def sql_escape(value):
    return value.replace("'", "''")

def main():
    import os
    dest = pathlib.Path(__file__).resolve().parent.parent / 'local_users.sql'
    if dest.exists():
        print('local_users.sql already exists; move or delete it before generating new accounts.')
        return
    statements = ['-- PRIVATE LOCAL PASSWORD HASHES. DO NOT COMMIT.\nUSE globaltrade;\n']
    for username, role, display in ACCOUNTS:
        pw = getpass.getpass(f'New password for {username} (blank to skip): ')
        if not pw:
            continue
        confirmation = getpass.getpass(f'Confirm password for {username}: ')
        if pw != confirmation:
            raise SystemExit(f'Password mismatch for {username}; no file written.')
        salt = os.urandom(16)
        iterations = 120000
        derived = hashlib.pbkdf2_hmac('sha256', pw.encode('utf-8'), salt, iterations, 32)
        password_hash = 'pbkdf2$120000$' + base64.b64encode(salt).decode() + '$' + base64.b64encode(derived).decode()
        stmt = ("INSERT INTO users(username,password_hash,full_name,email,active,role_id)\n"
                f"SELECT '{sql_escape(username)}','{password_hash}','{sql_escape(display)}',NULL,TRUE,r.id FROM roles r WHERE r.role_name='{sql_escape(role)}'\n"
                "ON DUPLICATE KEY UPDATE password_hash=VALUES(password_hash), full_name=VALUES(full_name), role_id=VALUES(role_id), active=TRUE;\n")
        statements.append(stmt)
    if len(statements) == 1:
        print('No accounts selected. No SQL generated.')
        return
    dest.write_text('\n'.join(statements), encoding='utf-8')
    try:
        os.chmod(dest, 0o600)
    except OSError:
        pass
    print(f'Private account SQL created: {dest}')
    print('Import it into MySQL locally. Never push local_users.sql to GitHub.')

if __name__ == '__main__':
    main()
