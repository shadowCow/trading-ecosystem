module.exports = function(grunt) {

    var path = require('path');

    require('load-grunt-config')(grunt, {
      configPath: path.join(process.cwd(), 'dev-tools/grunt/config'),
      jitGrunt: {
        customTasksDir: 'dev-tools/grunt/tasks'
      }
    });

};
