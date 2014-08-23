;;; Directory Local Variables
;;; See Info node `(emacs) Directory Variables' for more information.

((nil
  (compile-command . (concat "make -k -C " (let ((l (dir-locals-find-file (or (buffer-file-name) default-directory)))) (if (listp l) (car l) l)))))
 (java-mode
  (eval . (ede-java-root-project "COSMOTRON"
		       :name "cosmotron"
		       :version "1.0"
		       :mailinglist "dventimi@gmail.com"
		       :web-site-url "http://www.neptunestation.org"
		       :file "/home/dventimi/work/cosmotron/README"
		       :srcroot '("src")
		       :localclasspath nil))))
