(ns slack-dice.schemas
  (:require [schema.core :as schema]))

(import 'org.apache.commons.validator.routines.UrlValidator)

(def hex-color
  #"^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")

(def supported-image-types
  #"^.*\.(gif|jpeg|jpg|png|bmp)$")

(defn valid-color?
  [value]
  (or (re-matches hex-color value)
      (= "good" value)
      (= "warning" value)
      (= "danger" value)))

(defn valid-url?
  [s]
  (let [validator (UrlValidator.)]
    (.isValid validator s)))

(defn valid-image?
  [s]
  (and (valid-url? s)
       (re-matches supported-image-types s)))

(def Field
  {:title schema/Str
   :value schema/Str
   (schema/optional-key :short) schema/Bool})

(def Attachment
  {:text schema/Str
   (schema/optional-key :fallback) schema/Str
   (schema/optional-key :color) (schema/pred valid-color?)
   (schema/optional-key :pretext) schema/Str
   (schema/optional-key :author_name) schema/Str
   (schema/optional-key :author_link) (schema/pred valid-url?)
   (schema/optional-key :author_icon) (schema/pred valid-url?)
   (schema/optional-key :title) schema/Str
   (schema/optional-key :title_link) (schema/pred valid-url?)
   (schema/optional-key :fields) [Field]
   (schema/optional-key :image_url) (schema/pred valid-image?)
   (schema/optional-key :thumb_url) (schema/pred valid-image?)
   (schema/optional-key :footer) schema/Str
   (schema/optional-key :footer_icon) (schema/pred valid-url?)
   (schema/optional-key :ts) schema/Int})

(def Message
  {:attachments [Attachment]})
