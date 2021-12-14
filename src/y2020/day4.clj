(ns y2020.day4
  (:require [util]
            [clojure.spec.alpha :as s]))

(def input-val
  (-> (slurp "resources/y2020/day4.txt")
      (clojure.string/split #"\n\n")))

(defn parse-int-if-available [x]
  (try
     (Integer/parseInt x)
     (catch Exception e x)))


(def required-keys #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn parse-values [{:keys [passport/byr passport/cid passport/ecl passport/eyr
                            passport/hcl passport/hgt passport/iyr passport/pid]}]
  #:passport{:byr (parse-int-if-available byr)
             :iyr (parse-int-if-available iyr)
             :eyr (parse-int-if-available eyr)
             :pid pid
             :hcl hcl
             :hgt hgt
             :ecl (keyword ecl)
             :cid cid})

(defn ->passport-map [xs]
  "
  key:value 형태의 string의 sequence로 들어오는 입력값을 map 형태로 변환합니다.
  Input: [\"hcl:#bc352c\" \"pid:321838059\" \"byr:1930\" \"hgt:178cm\" \"cid:213\" \"eyr:2023\" \"ecl:amb\" \"iyr:2017\"]
  Output: #:passport{:byr \"1930\", :cid \"213\", :ecl \"amb\", :eyr \"2023\", :hcl \"#bc352c\", :hgt \"178cm\", :iyr \"2017\", :pid \"321838059\"}
  "
  (->> xs
       (map #(clojure.string/split % #":"))
       (map (fn [[k v]] [(keyword "passport" k) v]))
       (into (sorted-map))
       parse-values))
;(->passport-map ["hcl:#bc352c" "pid:321838059" "byr:1930" "hgt:178cm" "cid:213" "eyr:2023" "ecl:amb" "iyr:2017"])

(defn parse [x]
  (->> x
       (map #(clojure.string/split % #"\s"))
       (map ->passport-map)))

(defn has-required-keys? [m]
  (every? (set (keys m)) required-keys))

(s/def :passport/eye-colors #{:amb :blu :brn :gry :grn :hzl :oth})

(defn num-in-range? [num start end]
  (<= start num end))

(defn valid-height? [s]
  (if-let [[[_, num, unit]] (re-seq #"^(\d+)(cm|in)$" s)]
    (let [num (Integer/parseInt num)]
      (cond
        (= unit "cm") (<= 150 num 193)
        (= unit "in") (<= 59 num 76)))
    false))

(s/def :passport/byr (s/and int? #(num-in-range? % 1920 2002)))
(s/def :passport/iyr (s/and int? #(num-in-range? % 2010 2020)))
(s/def :passport/eyr (s/and int? #(num-in-range? % 2020 2030)))
(s/def :passport/hgt (s/and string? valid-height?))
(s/def :passport/hcl (s/and string? #(re-matches #"^#[a-f|0-9]{6}$" %)))
(s/def :passport/ecl (s/and keyword? #(s/valid? :passport/eye-colors %)))
(s/def :passport/pid (s/and string? #(re-matches #"^\d{9}$" %)))
(s/def :passport/passport (s/keys :req [:passport/byr :passport/iyr :passport/eyr :passport/hgt
                                        :passport/hcl :passport/ecl :passport/pid]
                                  :opt [:passport/cid]))

(comment
  ; Part 1
  (->> input-val
       parse
       (filter has-required-keys?)
       count)

  ; Part 2
  (->> input-val
       parse
       (filter #(s/valid? :passport/passport %))
       count))
