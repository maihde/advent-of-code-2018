import Foundation

let argCount = CommandLine.argc

let defaultFile = "../dat/day/1/input.txt";
let inputFile = (argCount == 1) ? defaultFile : CommandLine.arguments[0]

/*
Amazingly, there isn't a built-in way for a Swift application to 
read a file line-by-line!  If you did want to open a file it would
look something like this...but instead we will use the StreamReader
snippet from https://gist.github.com/klgraham/6fe11ed1e3fe075f5ffc8b7ca350bce4
*/

guard let inputStream = StreamReader(path: inputFile) else {
  print("Failed to open file " + inputFile)
  exit(-1)
}
defer { inputStream.close() }

var ans1: Int? = nil
var ans2: Int? = nil 
var accum = 0
var seen = Set<Int>()

while ans2 == nil {
  if let line = inputStream.nextLine() {
    if let val = Int(line) {
      accum += val;
    }
    if seen.contains(accum) && ans2 == nil {
      ans2 = accum
    } else {
      seen.insert(accum)
    }
  } else {
    if ans1 == nil {
      ans1 = accum
    }
    inputStream.rewind()
  }
}

/*
See this article for a discussion on optionals an string interpolation
https://oleb.net/blog/2016/12/optionals-string-interpolation/
*/
infix operator ???: NilCoalescingPrecedence

public func ???<T>(optional: T?, defaultValue: @autoclosure () -> String) -> String {
    switch optional {
    case let value?: return String(describing: value)
    case nil: return defaultValue()
    }
}

print("\(ans1 ??? "unknown") \(ans2 ??? "unknown")")


