module.exports = {
  options: {
    // this allows grunt to delete files outside the Gruntfile directory.
    // required to clean the ../public directory.
    force: true
  },
  dist: {
    src: ['dist/**/*', '!dist/.gitignore']
  }
}
