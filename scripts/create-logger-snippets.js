let levels = ['trace', 'debug', 'info', 'warn', 'error']

for (let level of levels) {
    let upperLevel = level.substring(0,1).toUpperCase() + level.substring(1)
    console.log(`
        @Override
        default void ${level}(String msg) {
            message("${level}", msg);
        }

        @Override
        default void ${level}(String format, Object arg) {
            message("${level}", format, arg);
        }

        @Override
        default void ${level}(String format, Object arg1, Object arg2) {
            message("${level}", format, arg1, arg2);
        }

        @Override
        default void ${level}(String format, Object... arguments) {
            message("${level}", format, arguments);
        }

        @Override
        default void ${level}(String msg, Throwable t) {
            message("${level}", msg, t);
        }

        @Override
        default void ${level}(Marker marker, String msg) {
            ${level}(msg);
        }

        @Override
        default void ${level}(Marker marker, String format, Object arg) {
            ${level}(format, arg);
        }

        @Override
        default void ${level}(Marker marker, String format, Object arg1, Object arg2) {
            ${level}(format, arg1, arg2);
        }

        @Override
        default void ${level}(Marker marker, String format, Object... argArray) {
            ${level}(format, argArray);
        }

        @Override
        default void ${level}(Marker marker, String msg, Throwable t) {
            ${level}(msg, t);
        }
        `)
}
