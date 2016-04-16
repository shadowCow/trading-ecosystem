module.exports = function(grunt) {
  grunt.registerTask('default', ['build', 'bs-dist']);
  grunt.registerTask('build', ['clean:dist','jade:compile', 'shell:tsCompile', 'sass:dist', 'postcss:dist']);
  grunt.registerTask('bs-dist', ['browserSync:dist', 'watch']);
}
