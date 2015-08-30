import groovy.json.JsonSlurper

class ModelParser {

  def parseModel(filename) {
    def jsonSlurper = new JsonSlurper()
    def model = jsonSlurper.parseText(new File(filename).text)

    assert model instanceof Map
    return model
  }

  static def main(args) {
    ModelParser modelParser = new ModelParser()
    def filename = "../resources/model.json"
    def model = modelParser.parseModel(filename)
    println model
  }
}
