class VgLang < Formula
  desc "VG Language interpreter and package manager"
  homepage "https://github.com/vg-lang/vg-lang"
  url "https://github.com/vg-lang/vg-lang/releases/download/v1.5.0/vg-lang-1.5.0.tar.gz"
  sha256 "REPLACE_ME" 
  license "MIT"


  livecheck do
    url :stable
    regex(/^v?(\d+(?:\.\d+)+)$/i)
  end

  depends_on "openjdk@21"

  def install

    libexec.install "vg.jar", "vgpkg.jar", "libraries", "config"

    java_home = Formula["openjdk@21"].opt_prefix

    (bin/"vg").write <<~EOS
      #!/bin/bash
      export VG_APP_CONFIG="#{libexec}/config"
      export VG_LIBRARIES_PATH="#{libexec}/libraries"
      exec "#{java_home}/bin/java" -jar "#{libexec}/vg.jar" "$@"
    EOS

    (bin/"vgpkg").write <<~EOS
      #!/bin/bash
      export VG_APP_CONFIG="#{libexec}/config"
      export VG_LIBRARIES_PATH="#{libexec}/libraries"
      exec "#{java_home}/bin/java" -jar "#{libexec}/vgpkg.jar" "$@"
    EOS
  end

  test do
    assert_match "VG Version", shell_output("#{bin}/vg --version")
  end
end
