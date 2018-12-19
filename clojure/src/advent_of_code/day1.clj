;; Defines the namespace; because we include :gen-class this creates a Java
;; class advent_of_code.day1 that inherits from Object.  By default this maps
;; clojure functions with a prefix of '-' to equivalent Java functions.
(ns advent-of-code.day1
  (:gen-class))

;; Load the clojure.java.io library.  The single quote in front of the
;; vector basically quotes all of the internal items of the vector
(require '[clojure.java.io :as io])

(defn first-duplicate [coll]
  "Return the first duplicate value in coll"
  (reduce
    ;; the reduce function takes the accumulator and the current value
    ;; if the accumulator already contains the value x, then reduce
    ;; is aborted early with the value x, other wise x is added
    ;; to the accumulator.  The collection can be of infinite length
    ;; but memory usage will be O(n) with the number of unique
    ;; values found in coll
    (fn [acc x] (if (contains? acc x) (reduced x) (conj acc x)))
    ;; reduce initial val and collection
    (hash-set nil) coll
  )
)

;; Main function
(defn -main
  "Solve advent of code day 1"
  [& args]

  ;; Part 1

  ;; Open a file reader
  (with-open [rdr (io/reader "../dat/day/1/input.txt")]
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
  (with-open [rdr (io/reader "../dat/day/1/input.txt")]
    ;; iterate over all lines using line-seq into the lines iterator
    (let [ lines (line-seq rdr) dups (hash-set nil) ]
      ;; use cycle to continual loop over lines, converting them to a number,
      ;; and then doing a reductions on it to produce a running sum
      (def ans
        (first-duplicate (reductions + (map #(Long/parseLong %) (cycle lines))))
      )
      ;; Print the answer
      (println ans)
    )
  )
)
