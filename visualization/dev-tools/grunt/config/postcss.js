module.exports = {
  options: {
    // no sourcemaps
    map: false,
    processors: [
      // add vendor prefixes
      require('autoprefixer')({browsers: 'last 2 versions'})
    ]
  },
  dist: {
    src: 'dist/**/*.css'
  }
}
