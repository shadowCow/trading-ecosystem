module.exports = {
  compile: {
      options: {
        pretty: true
      },
      files: [{
        cwd: "src",
        src: ["**/*.jade", "!index.jade"],
        dest: "dist",
        expand: true,
        ext: ".html"
      },{
        cwd: "src",
        src: "index.jade",
        dest: "./",
        expand: true,
        ext: ".html"
      }]
  }
}
