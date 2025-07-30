import { Download, Code, BookOpen, Package, Play } from 'lucide-react'
import Link from 'next/link'

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
      {/* Navigation */}
      <nav className="border-b border-gray-700 bg-gray-800/50 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-4">
              <div className="text-2xl font-bold text-yellow-400">VG</div>
              <div className="text-sm text-gray-300">Language</div>
            </div>
            <div className="hidden md:flex items-center space-x-8">
              <Link href="https://docs.vglang.com" className="text-gray-300 hover:text-yellow-400 transition-colors">
                Documentation
              </Link>
              <Link href="https://packages.vglang.com" className="text-gray-300 hover:text-yellow-400 transition-colors">
                Packages
              </Link>
              <Link href="https://github.com/Husseinabdulameer11/vg-lang" className="text-gray-300 hover:text-yellow-400 transition-colors">
                GitHub
              </Link>
            </div>
          </div>
        </div>
      </nav>

      {/* Hero Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="text-center">
          <h1 className="text-5xl md:text-7xl font-bold text-white mb-6">
            VG Language
          </h1>
          <p className="text-xl md:text-2xl text-gray-300 mb-8 max-w-3xl mx-auto">
            A lightweight, expressive programming language designed for simplicity and ease of use.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link 
              href="https://github.com/Husseinabdulameer11/vg-lang/releases/latest/download/VGSetup.exe"
              className="inline-flex items-center px-8 py-4 bg-yellow-500 hover:bg-yellow-600 text-gray-900 font-semibold rounded-lg transition-colors"
            >
              <Download className="w-5 h-5 mr-2" />
              Download VG Language
            </Link>
            <Link 
              href="https://docs.vglang.com"
              className="inline-flex items-center px-8 py-4 border border-gray-600 text-white hover:bg-gray-700 font-semibold rounded-lg transition-colors"
            >
              <BookOpen className="w-5 h-5 mr-2" />
              View Documentation
            </Link>
          </div>
        </div>
      </div>

      {/* Features Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="grid md:grid-cols-3 gap-8">
          <div className="bg-gray-800 p-8 rounded-xl border border-gray-700">
            <Code className="w-12 h-12 text-yellow-400 mb-4" />
            <h3 className="text-xl font-semibold text-white mb-3">Simple Syntax</h3>
            <p className="text-gray-300">
              Easy to learn and write with intuitive syntax that makes coding accessible to everyone.
            </p>
          </div>
          <div className="bg-gray-800 p-8 rounded-xl border border-gray-700">
            <Package className="w-12 h-12 text-yellow-400 mb-4" />
            <h3 className="text-xl font-semibold text-white mb-3">Rich Libraries</h3>
            <p className="text-gray-300">
              Comprehensive standard library with built-in functions for common programming tasks.
            </p>
          </div>
          <div className="bg-gray-800 p-8 rounded-xl border border-gray-700">
            <Play className="w-12 h-12 text-yellow-400 mb-4" />
            <h3 className="text-xl font-semibold text-white mb-3">Fast Execution</h3>
            <p className="text-gray-300">
              Optimized interpreter for quick development and efficient runtime performance.
            </p>
          </div>
        </div>
      </div>

      {/* Code Example Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="bg-gray-800 rounded-xl border border-gray-700 p-8">
          <h2 className="text-2xl font-bold text-white mb-6">Quick Start</h2>
          <div className="bg-gray-900 rounded-lg p-6 font-mono text-sm">
            <div className="text-gray-400 mb-2">## Hello World in VG Language</div>
            <div className="text-yellow-400">print</div>
            <div className="text-white">"Hello, World!"</div>
            <div className="text-gray-400 mt-4 mb-2">## Variables and Loops</div>
            <div className="text-yellow-400">var</div>
            <div className="text-white">count = 0</div>
            <div className="text-yellow-400">while</div>
            <div className="text-white">count &lt; 5</div>
            <div className="text-yellow-400">print</div>
            <div className="text-white">"Count: " + count</div>
            <div className="text-white">count = count + 1</div>
            <div className="text-yellow-400">end</div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="border-t border-gray-700 bg-gray-800/50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid md:grid-cols-4 gap-8">
            <div>
              <div className="text-2xl font-bold text-yellow-400 mb-4">VG Language</div>
              <p className="text-gray-300">
                Simple, expressive programming for everyone.
              </p>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Resources</h3>
              <ul className="space-y-2 text-gray-300">
                <li><Link href="https://docs.vglang.com" className="hover:text-yellow-400 transition-colors">Documentation</Link></li>
                <li><Link href="https://packages.vglang.com" className="hover:text-yellow-400 transition-colors">Packages</Link></li>
                <li><Link href="https://github.com/Husseinabdulameer11/vg-lang" className="hover:text-yellow-400 transition-colors">GitHub</Link></li>
              </ul>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Community</h3>
              <ul className="space-y-2 text-gray-300">
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Discord</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Twitter</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Blog</Link></li>
              </ul>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Legal</h3>
              <ul className="space-y-2 text-gray-300">
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Privacy</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Terms</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">License</Link></li>
              </ul>
            </div>
          </div>
          <div className="border-t border-gray-700 mt-8 pt-8 text-center text-gray-400">
            <p>&copy; 2024 VG Language. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  )
} 