module.exports = {
  dist: {
    files: [
      {'dist/css/styles.css': 'src/scss/styles.scss'},
      {
        expand: true,
        cwd: 'src/components',
        src: ['**/*.scss'],
        dest: 'dist/components',
        ext: '.css'
      }
    ],
    options: {
      sourcemap: 'none',
      style: 'expanded',
      require: 'susy'
    }
  }
}
