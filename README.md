Borg-runner is a simple Java application for automating borg backups.

Run the application with one argument that points to a json settings file. Below is an example settings file with the
minimal required arguments for the application to run:

```json
{
  "dryRun": false,
  "archives": [
    {
      "directories": [
        "/home/user/dir"
      ],
      "name": "backup"
    }
  ]
}
```

With this setup, you must have environment variables `BORG_REPO` and `BORG_PASSPHRASE` set. These environment variables
will be ignored if the respective settings fields are set. For these and many more options, here is an example settings 
file with all possible fields set:

```json
{
"repo": "repo address",
"passphrase": "repo passphrase",
"prefix": "{hostname}-",
"suffix": "-{now}",
"pruneSuffix": "-*",
"dryRun": false,
"extraFlags": [
    "--show-rc"
],
"extraCompactFlags": [
    "--verbose"
],
"archives": [
    {
        "directories": [
            "/home/user/dir",
            "/home/user/other"
        ],
        "name": "backup",
        "createStats": true,
        "createList": true,
        "filter": "AMCE",
        "exclude": [
            "*.7z",
            "**/build"
        ],
        "excludeFrom": [
            "/home/user/exclude.txt"
        ],
        "excludeCaches": true,
        "excludeIfPresent": [
            "EXCLUDE.FILE"
        ],
        "keepExcludeTags": false,
        "extraCreateFlags": [
            "--verbose"
        ],
        "pruneStats": true,
        "pruneList": true,
        "keepWithin": "30d",
        "keepSecondly": 1,
        "keepMinutely": 0,
        "keepHourly": 1,
        "keepDaily": 7,
        "keepWeekly": 5,
        "keepMonthly": 12,
        "keepYearly": 10,
        "extraPruneFlags": [
            "--verbose"
        ]
    }
]
}
```

The application checks for the presence of environment variables `SSH_AUTH_SOCK` and `SSH_AGENT_PID` for SSH 
authentication. If either of them is not defined, [keychain](https://www.funtoo.org/Funtoo:Keychain) will be used 
instead. In this case keychain needs to be installed, in PATH and with your SSH key added to it for the application to 
work.