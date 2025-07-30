import { Search, Package, Download, Star, Calendar } from 'lucide-react'
import Link from 'next/link'

// Mock package data
const packages = [
  {
    name: 'Arrays',
    description: 'Advanced array manipulation and utility functions',
    version: '1.2.0',
    downloads: 15420,
    stars: 45,
    updated: '2024-01-15',
    author: 'VG Team'
  },
  {
    name: 'DateTime',
    description: 'Date and time manipulation utilities',
    version: '2.1.0',
    downloads: 8920,
    stars: 32,
    updated: '2024-01-10',
    author: 'VG Team'
  },
  {
    name: 'IO',
    description: 'File and console input/output operations',
    version: '1.5.0',
    downloads: 12340,
    stars: 28,
    updated: '2024-01-12',
    author: 'VG Team'
  },
  {
    name: 'Mathlib',
    description: 'Mathematical functions and calculations',
    version: '1.8.0',
    downloads: 9870,
    stars: 41,
    updated: '2024-01-08',
    author: 'VG Team'
  },
  {
    name: 'OsLib',
    description: 'Operating system interface and utilities',
    version: '1.3.0',
    downloads: 6540,
    stars: 19,
    updated: '2024-01-14',
    author: 'VG Team'
  },
  {
    name: 'Random',
    description: 'Random number generation and utilities',
    version: '1.1.0',
    downloads: 5430,
    stars: 15,
    updated: '2024-01-11',
    author: 'VG Team'
  },
  {
    name: 'Util',
    description: 'General utility functions and helpers',
    version: '1.4.0',
    downloads: 11230,
    stars: 37,
    updated: '2024-01-09',
    author: 'VG Team'
  }
]

export default function PackagesHome() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900">
      {/* Navigation */}
      <nav className="border-b border-gray-700 bg-gray-800/50 backdrop-blur-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center space-x-4">
              <div className="text-2xl font-bold text-yellow-400">VG</div>
              <div className="text-sm text-gray-300">Packages</div>
            </div>
            <div className="hidden md:flex items-center space-x-8">
              <Link href="https://vglang.com" className="text-gray-300 hover:text-yellow-400 transition-colors">
                Home
              </Link>
              <Link href="https://docs.vglang.com" className="text-gray-300 hover:text-yellow-400 transition-colors">
                Documentation
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
            VG Packages
          </h1>
          <p className="text-xl md:text-2xl text-gray-300 mb-8 max-w-3xl mx-auto">
            Discover and install packages for the VG Language ecosystem.
          </p>
          
          {/* Search Bar */}
          <div className="max-w-2xl mx-auto mb-12">
            <div className="relative">
              <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder="Search packages..."
                className="w-full pl-12 pr-4 py-4 bg-gray-800 border border-gray-600 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:border-yellow-500 transition-colors"
              />
            </div>
          </div>
        </div>
      </div>

      {/* Packages Grid */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {packages.map((pkg) => (
            <div key={pkg.name} className="bg-gray-800 p-6 rounded-xl border border-gray-700 hover:border-yellow-500 transition-colors">
              <div className="flex items-start justify-between mb-4">
                <div className="flex items-center space-x-2">
                  <Package className="w-5 h-5 text-yellow-400" />
                  <h3 className="text-lg font-semibold text-white">{pkg.name}</h3>
                </div>
                <span className="text-xs bg-yellow-500 text-gray-900 px-2 py-1 rounded-full font-semibold">
                  v{pkg.version}
                </span>
              </div>
              
              <p className="text-gray-300 mb-4 text-sm">
                {pkg.description}
              </p>
              
              <div className="flex items-center justify-between text-xs text-gray-400 mb-4">
                <div className="flex items-center space-x-4">
                  <div className="flex items-center space-x-1">
                    <Download className="w-4 h-4" />
                    <span>{pkg.downloads.toLocaleString()}</span>
                  </div>
                  <div className="flex items-center space-x-1">
                    <Star className="w-4 h-4" />
                    <span>{pkg.stars}</span>
                  </div>
                </div>
                <div className="flex items-center space-x-1">
                  <Calendar className="w-4 h-4" />
                  <span>{pkg.updated}</span>
                </div>
              </div>
              
              <div className="flex items-center justify-between">
                <span className="text-xs text-gray-400">by {pkg.author}</span>
                <button className="text-yellow-400 hover:text-yellow-300 text-sm font-semibold transition-colors">
                  Install
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Stats Section */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div className="bg-gray-800 rounded-xl border border-gray-700 p-8">
          <h2 className="text-2xl font-bold text-white mb-6">Package Registry Stats</h2>
          <div className="grid md:grid-cols-4 gap-8">
            <div className="text-center">
              <div className="text-3xl font-bold text-yellow-400 mb-2">7</div>
              <div className="text-gray-300">Total Packages</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-yellow-400 mb-2">68,850</div>
              <div className="text-gray-300">Total Downloads</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-yellow-400 mb-2">209</div>
              <div className="text-gray-300">Total Stars</div>
            </div>
            <div className="text-center">
              <div className="text-3xl font-bold text-yellow-400 mb-2">1</div>
              <div className="text-gray-300">Active Maintainers</div>
            </div>
          </div>
        </div>
      </div>

      {/* Footer */}
      <footer className="border-t border-gray-700 bg-gray-800/50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div className="grid md:grid-cols-4 gap-8">
            <div>
              <div className="text-2xl font-bold text-yellow-400 mb-4">VG Packages</div>
              <p className="text-gray-300">
                The official package registry for VG Language.
              </p>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Resources</h3>
              <ul className="space-y-2 text-gray-300">
                <li><Link href="https://docs.vglang.com" className="hover:text-yellow-400 transition-colors">Documentation</Link></li>
                <li><Link href="https://vglang.com" className="hover:text-yellow-400 transition-colors">Home</Link></li>
                <li><Link href="https://github.com/Husseinabdulameer11/vg-lang" className="hover:text-yellow-400 transition-colors">GitHub</Link></li>
              </ul>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Developers</h3>
              <ul className="space-y-2 text-gray-300">
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Publish Package</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">API</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">CLI</Link></li>
              </ul>
            </div>
            <div>
              <h3 className="text-white font-semibold mb-4">Support</h3>
              <ul className="space-y-2 text-gray-300">
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Help</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Report Issue</Link></li>
                <li><Link href="#" className="hover:text-yellow-400 transition-colors">Contact</Link></li>
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