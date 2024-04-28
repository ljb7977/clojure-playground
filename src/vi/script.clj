(ns vi.script
  (:require [hato.client :as hc]
            [clojure.string]
            [jsonista.core :as j]))
  
(comment
  (let [input "남부대학교\n위덕대학교\n중앙승가대학교\n한신대학교\n수원대학교\n예원예술대학교\n전주대학교\n고신대학교\n금강대학교\n한라대학교\n대구공업대학교\n우송정보대학"]
    #_(count (clojure.string/split-lines input))
    (->> (clojure.string/split-lines input)
         (map #(str "'" % "'"))
         (clojure.string/join ", ")))
  
  (def data 
    (doall (for [i (range 1 22)]
             (let [request-params {:pageIndex i
                                   :pageSize 10
                                   :nppCdLst []
                                   :regionCdLst []
                                   :univKindLst []
                                   :eiName ""
                                   :langCode "ko"}
                   data (hc/post
                          "https://www.studyinkorea.go.kr/ko/study/srchCollegeAjax.do"
                          {:form-params {:jsonStr
                                         (j/write-value-as-string request-params)}})]
               (-> (:body data)
                   (java.net.URLDecoder/decode "UTF-8")
                   (j/read-value))))))
  (let [names (->> data
                   flatten
                   (map #(get % "eiName"))
                   (map #(str "'" % "'")))]
    (as-> (clojure.string/join ", " names) $
          (str "(" $ ")")))
  ,)
