let levels = ['trace', 'debug', 'info', 'warn', 'error']

for (let level of levels) {
    let upperLevel = level.substring(0,1).toUpperCase() + level.substring(1)
    console.log(`
        public static void ${level}(String msg) {
            getLogger().${level}(msg);
        }

        public static void ${level}(String format, Object arg) {
            getLogger().${level}(format, arg);
        }

        public static void ${level}(String format, Object arg1, Object arg2) {
            getLogger().${level}(format, arg1, arg2);
        }

        public static void ${level}(String format, Object... argArray) {
            getLogger().${level}(format, argArray);
        }

        public static void ${level}(String msg, Throwable t) {
            getLogger().${level}(msg, t);
        }

        public static void ${level}(Marker marker, String msg) {
            getLogger().${level}(marker, msg);
        }

        public static void ${level}(Marker marker, String format, Object arg) {
            getLogger().${level}(marker, format, arg);
        }

        public static void ${level}(Marker marker, String format, Object arg1, Object arg2) {
            getLogger().${level}(marker, format, arg1, arg2);
        }

        public static void ${level}(Marker marker, String format, Object... argArray) {
            getLogger().${level}(marker, format, argArray);
        }

        public static void ${level}(Marker marker, String msg, Throwable t) {
            getLogger().${level}(marker, msg, t);
        }

        public static boolean is${upperLevel}Enabled() {
            return getLogger().is${upperLevel}Enabled();
        }

        public static boolean is${upperLevel}Enabled(Marker marker) {
            return getLogger().is${upperLevel}Enabled();
        }



        `)
}


console.log(`
        public static IEventLogger keyword(@NotNull String... keywords) {
            return getLogger().keyword(keywords);
        }

        public static IEventLogger withK(@NotNull String... keywords) {
            return keyword(keywords);
        }
    `)
