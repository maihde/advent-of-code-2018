;; Defines the namespace; because we include :gen-class this creates a Java
;; class advent_of_code.day1 that inherits from Object.  By default this maps
;; clojure functions with a prefix of '-' to equivalent Java functions.
(ns advent-of-code.day1
  (:gen-class))

;; Load the clojure.java.io library.  The single quote in front of the
;; vector basically quotes all of the internal items of the vector
(require '[clojure.java.io :as io])

(defn firstdup [nums, dups, cursum, idx]
  ;; calculate the next sum
  (def sum (+ cursum (nth nums idx) ) )
  ;; if we have seen this sum already return it
  ;; otherwise recurse
  (if (contains? dups sum) sum (do
    (def ndups (conj dups sum))
    (let [ ii (mod (+ idx 1) (count nums) ) ]
      (recur nums ndups sum ii)
    )
  ))
)

;; Main function
(defn -main
  "Solve advent of code day 1"
  [& args]

  ;; Part 1

  ;; Open a file reader
  (with-open [rdr (io/reader "dat/day/1/input.txt")]
    ;; iterate over all lines using line-seq into the lines iterator
    (let [ lines (line-seq rdr) ]
      ;; parse each line into a long, sum them together set the value to ans
      (let [ ans (reduce + ( map #(Long/parseLong %) lines) ) ]
        ;; print the answer
        (println ans)
      )
    )
  )

  ;; Part 2

  ;; Open a file reader
  (with-open [rdr (io/reader "dat/day/1/input.txt")]
    ;; iterate over all lines using line-seq into the lines iterator
    (let [lines (line-seq rdr)]
      ;; parse each line into a long store into a vector nums
      ;; this approach wouldn't work very well if the list of numbers
      ;; exceeded the system memory
      (let [ nums ( map #(Long/parseLong %) lines) ] 
        ;; Find the first duplicate sum using tail recursion
        (def ans ( firstdup nums (hash-set nil) 0 0 ) )
        ;; Print the answer
        (println ans)
      )
    )
  )
)
