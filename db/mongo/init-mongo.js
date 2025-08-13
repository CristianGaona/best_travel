db.createUser({
        user: 'root',
        pwd: 'toor',
        roles: [
            {
                role: 'readWrite',
                db: 'testDB',
            },
        ],
    });
db.createCollection('app_users', { capped: false });

db.app_users.insert([
    { 
        "username": "ragnar777", 
        "dni": "VIKI771012HMCRG093", 
        "enabled": true, 
        "password": "$2a$10$1qv7EpnklKhkHSH.sFqXwuyCMLOovCGDNsIKQ7emABJmRcdXWqSj2", 
        "role": 
        {
            "granted_authorities": ["ROLE_USER"]
        } 
    },
    { 
        "username": "heisenberg", 
        "dni": "BBMB771012HMCRR022", 
        "enabled": true, 
        "password": "$2a$10$BxLiQAKFZnQ9aHR2tR38vuoIz7GZcCajUrhmQNGnHwb8eDdATthaa", 
        "role": 
        {
            "granted_authorities": ["ROLE_USER"]
        } 
    },
    { 
        "username": "misterX", 
        "dni": "GOTW771012HMRGR087", 
        "enabled": true, 
        "password": "$2a$10$mO7RKbMjteGrq/BCxMYqpuq61wOX.KuuXGKTfGp6DZvJwOZ.LvfqC", 
        "role": 
        {
            "granted_authorities": ["ROLE_USER", "ROLE_ADMIN"]
        } 
    },
    { 
        "username": "neverMore", 
        "dni": "WALA771012HCRGR054", 
        "enabled": true, 
        "password": "$2a$10$0bp9dG5mNTPnn4BwPIP2x.McloPn3hnX8hHcxYWhQBpBRGcAv7bCe", 
        "role": 
        {
            "granted_authorities": ["ROLE_ADMIN"]
        } 
    }
]);