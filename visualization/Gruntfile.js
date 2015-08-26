'use strict';

module.exports = function(grunt) {

    grunt.initConfig({

    });

    grunt.registerTask('build-test', 'Test gradle-grunt integration', function() {
        grunt.log.writeln('executing grunt custom build.');
    });
}
