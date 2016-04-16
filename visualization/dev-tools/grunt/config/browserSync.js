module.exports = {
  // make sure to call the 'browserSync:xxxx' task rather than just 'browserSync' so it doesn't try to serve multiple things
  dist: {
    options: {
      server: './',
      // background must be true in order for grunt watch task to run
      background: true,
      browser: 'google chrome'
    }
  }
}
