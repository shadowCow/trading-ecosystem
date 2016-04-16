module.exports = {
  options: {
    // spawn must be false for bsReload tasks to work correctly
    spawn: false
  },
  css: {
    files: ['src/scss/**/*.scss'],
    tasks: ['sass:dist', 'postcss:dist', 'bsReload:css']
  },
  typescript: {
    files: ['src/**/*.ts'],
    tasks: ['shell:tsCompile', 'bsReload:all']
  },
  html: {
    files: ['src/**/*.html'],
    tasks: ['copy:html', 'bsReload:all']
  },
  jade: {
    files: ['src/**/*.jade'],
    tasks: ['jade:compile', 'bsReload:all']
  }
}
