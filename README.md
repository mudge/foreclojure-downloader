# foreclojure-downloader

A Clojure library to download 4clojure problems into locally runnable
tests using [Midje](https://github.com/marick/Midje).

## Usage

Use `lein run` to create tests under `src/foreclojure_solutions` by default.
This can be overridden by passing a path to `run`, e.g. `lein run solutions`.

```console
$ git clone git://github.com/mudge/foreclojure-downloader.git
$ cd foreclojure-downloader
$ lein deps
$ lein run
```

## License

Copyright © 2013 Paul Mucur

Distributed under the Eclipse Public License, the same as Clojure.

