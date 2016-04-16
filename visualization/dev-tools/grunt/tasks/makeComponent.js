module.exports = function(grunt) {

  var makeComponentTaskDescription = 'Creates an an angular2 component at a location, composed of a typescript file, a jade file, and an scss file.  Takes 1 argument - the path to the target component directory from the current directory.  If the component directory does not exist, it will be created';

  grunt.registerTask('makeComponent', makeComponentTaskDescription, function(targetDirectory, componentSelector) {
    if (!targetDirectory.startsWith('src/components/')) {
      grunt.log.error('Target directory must begin with "src/components/"');
      // Fail the task
      return false;
    }

    grunt.log.writeln('Creating component [ ' + componentSelector.toLowerCase() + ' ] at [ ' + targetDirectory + ' ]');

    // define patterns we intend to replace, and the content we intend to replace them with.
    var replacementData = {
      componentSelectorPattern : /<%= component-selector %>/g,
      componentPathPattern : /<%= component-path %>/g,
      componentClassNamePattern : /<%= component-class-name %>/g,
      componentSelector : componentSelector.toLowerCase(),
      componentPath : targetDirectory.replace(/^src/, '/dist'),
      componentClassName : convertHyphenatedToCamelCase(componentSelector, true) + 'Component'
    }

    var tsFileContents = getProcessedTemplate(grunt, 'dev-tools/templates/component-template-ts.txt', replacementData);
    var jadeFileContents = getProcessedTemplate(grunt, 'dev-tools/templates/component-template-jade.txt', replacementData);
    var scssFileContents = getProcessedTemplate(grunt, 'dev-tools/templates/component-template-scss.txt', replacementData);

    var targetTsFile = targetDirectory + '/' + componentSelector + '.component.ts';
    var targetJadeFile = targetDirectory + '/' + componentSelector + '.jade';
    var targetScssFile = targetDirectory + '/' + componentSelector + '.scss';

    grunt.log.writeln('Templates processed, writing files...');

    grunt.file.write(targetTsFile, tsFileContents);
    grunt.log.writeln(targetTsFile + ' created');

    grunt.file.write(targetJadeFile, jadeFileContents);
    grunt.log.writeln(targetJadeFile + ' created');

    grunt.file.write(targetScssFile, scssFileContents);
    grunt.log.writeln(targetScssFile + ' created');

    grunt.log.writeln(componentSelector + ' component created successfully');
  });

  function convertHyphenatedToCamelCase(hyphenatedString, firstLetterShouldBeCapitalized) {
    var chunks = hyphenatedString.split('-');

    var camelCase = firstLetterShouldBeCapitalized ? capitalize(chunks[0].toLowerCase()) : chunks[0].toLowerCase();
    for (var i = 1; i < chunks.length; i++) {
      camelCase = camelCase + capitalize(chunks[i].toLowerCase());
    }

    return camelCase;
  }

  function capitalize(aString) {
    return aString.charAt(0).toUpperCase() + aString.substring(1);
  }

  function getProcessedTemplate(grunt, filePath, replacementData) {
    var template = grunt.file.read(filePath)
                    .replace(replacementData.componentSelectorPattern, replacementData.componentSelector)
                    .replace(replacementData.componentPathPattern, replacementData.componentPath)
                    .replace(replacementData.componentClassNamePattern, replacementData.componentClassName);

    return template;
  }
}
