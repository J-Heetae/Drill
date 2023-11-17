module.exports = {
  presets: ['module:metro-react-native-babel-preset'],
  plugins: [
		// react-native-dotenv
		[
			'module:react-native-dotenv',
			{
				"envName": "APP_ENV",
				"moduleName": "@env",
				"path": ".env.local",
				"blocklist": null,
				"allowlist": null,
				"blacklist": null, 
				"whitelist": null, 
				"safe": false,
				"allowUndefined": true,
				"verbose": false
			},
		],
	],
};
