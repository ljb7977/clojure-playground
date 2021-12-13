(ns y2018.day5
  (:require util))

(def input-val
  (first (util/read-lines "y2018/day5.txt")))

(def example-input "dabAcCaCBAcCcaDA")

(defn abs [n]
  (max n (- n)))

(defn same-letter-but-opposite-capitalization?
  "두 문자가 서로 같으면서 대소문자 여부만 다른지를 체크합니다.

  Input: a b
  Output: false

  Input: a A
  Output: true
  "
  [f g]
  (= (abs (- (int f) (int g))) 32))

(defn remove-matching-pairs
  "문자열 전체를 한번 스캔하면서 지울 수 있는 문자를 최대한 지웁니다.
  스택에 문자를 넣고, 그 다음 문자가 왔을 때 앞의 문자와 대소문자만 다르면 스택에서 제거하는 것을 반복합니다.
  Input: dabAcCaCBAcCcaDA
  Output: [d a b C B A c a D A]
  "
  [s]
  (reduce (fn [stack c]
            (let [last-char (peek stack)]
              (cond
                (nil? last-char) [c]
                (same-letter-but-opposite-capitalization? last-char c) (pop stack)
                :else (conj stack c))))
          []
          s))

(def all-alphabets "abcdefghijklmnopqrstuvwxyz")

(defn lower-case [c]
  (let [c-code (int c)]
    (if
      (<= 65 c-code 90)
      (char (+ c-code 32))
      c)))
; sequence는 thread-last, collection은 thread-first인 경향이 있다

(defn remove-alphabet-from-string
  "소문자 하나(c)와 문자열을 받아서, 문자열로부터 대소문자에 관계 없이 c를 모두 제거합니다.
  Input: c abcdedCCda
  Output: abdedda
  "
  [c s]
  (reduce (fn [acc v]
            (if (= c (lower-case v)) acc (str acc v))) "" s))

(defn remove-one-alphabet-and-react [a s]
  (->> s
       (remove-alphabet-from-string a)
       remove-matching-pairs))

(comment
  ; ---- Part 1
  (->> input-val
       remove-matching-pairs
       count)

  ; ---- Part 2
  (time (->>
          (for [a all-alphabets] (remove-one-alphabet-and-react a input-val))
          (pmap count)
          (apply min))))
