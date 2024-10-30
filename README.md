Borg-caller is a simple Java application for automating borg backups.

Run the application with one argument that points to a json settings file. Below is an example settings file:

```json
{
"repo": "repository address",
"pass": "repository passphrase",
"dirs": [
	{
		"dir": "/home/user/folder",
		"name": "folder",
		"excludes": [
			"*.7z",
			"*.mp4"
		],
		"daily": 7,
		"weekly": 5,
		"monthly": 6,
		"yearly": 10
	}
]
}
```

You can set up as many dirs as you want. The script will first run `borg create` for all dirs, then `borg prune` for all
dirs and finally `borg compact`. Each dir will have its own archive, with the archive name being 
`{hostname}-[dir.name]-{now}`.

Create will be run with arguments 
`--verbose --filter AMCE --list --stats --show-rc --compression lzma --exclude-caches` as well as 
`--exclude [dir.exclude[i]]` for every string in the `dir.exclude` array. 

Prune will be run with arguments `--list --glob-archives {hostname}-[dir.name]-* --show-rc` as well as 
`--keep-daily [dir.daily]`, `--keep-weekly [dir.weekly]`, `--keep-monthly [dir.monthly]` and 
`--keep-yearly [dir.yearly]` according to the dir settings, unless the integer is set to a number smaller than 1, in 
which case the whole argument is omitted.

Compact will be run with the `--verbose` argument.

The application checks for the presence of environment variables `SSH_AUTH_SOCK` and `SSH_AGENT_PID` for SSH 
authentication. If either of them is not defined, [keychain](https://www.funtoo.org/Funtoo:Keychain) will be used 
instead. In this case keychain needs to be installed, in PATH and with your SSH key added to it for the application to 
work.