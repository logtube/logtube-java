let levels = ['trace', 'debug', 'info', 'warn', 'error']

for (let level of levels) {
    let upperLevel = level.substring(0,1).toUpperCase() + level.substring(1)
    console.log(`
        public void ${level}(String msg) {
            getLogger().${level}(msg);
        }

        public void ${level}(String format, Object arg) {
            getLogger().${level}(format, arg);
        }

        public void ${level}(String format, Object arg1, Object arg2) {
            getLogger().${level}(format, arg1, arg2);
        }

        public void ${level}(String format, Object... argArray) {
            getLogger().${level}(format, argArray);
        }

        public void ${level}(String msg, Throwable t) {
            getLogger().${level}(msg, t);
        }

        public void ${level}(Marker marker, String msg) {
            getLogger().${level}(marker, msg);
        }

        public void ${level}(Marker marker, String format, Object arg) {
            getLogger().${level}(marker, format, arg);
        }

        public void ${level}(Marker marker, String format, Object arg1, Object arg2) {
            getLogger().${level}(marker, format, arg1, arg2);
        }

        public void ${level}(Marker marker, String format, Object... argArray) {
            getLogger().${level}(marker, format, argArray);
        }

        public void ${level}(Marker marker, String msg, Throwable t) {
            getLogger().${level}(marker, msg, t);
        }

        public boolean is${upperLevel}Enabled() {
            return getLogger().is${upperLevel}Enabled();
        }

        public boolean is${upperLevel}Enabled(Marker marker) {
            return getLogger().is${upperLevel}Enabled();
        }



        `)
}


console.log(`
public IEventLogger keyword(@NotNull String... keywords) {
            return getLogger().keyword(keywords);
        }

        public IEventLogger withK(@NotNull String... keywords) {
            return keyword(keywords);
        }

    `)
