#!/bin/sh
exec scala "$0" "$@"
!#

object HelloWorld extends App {
  if (args.length <= 0) {
    System.exit(1)
  }

  val typeName = args(0)

  #not done yet - read in templates, replace the type_name variable, and print the result to the appropriate directories
}
