import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'VG Packages - Package Registry',
  description: 'Discover and install packages for the VG Language ecosystem.',
  keywords: ['vg language', 'packages', 'registry', 'libraries'],
  authors: [{ name: 'VG Language Team' }],
  openGraph: {
    title: 'VG Packages - Package Registry',
    description: 'Discover and install packages for the VG Language ecosystem.',
    url: 'https://packages.vglang.com',
    siteName: 'VG Packages',
    images: [
      {
        url: '/og-image.png',
        width: 1200,
        height: 630,
        alt: 'VG Packages',
      },
    ],
    locale: 'en_US',
    type: 'website',
  },
  twitter: {
    card: 'summary_large_image',
    title: 'VG Packages - Package Registry',
    description: 'Discover and install packages for the VG Language ecosystem.',
    images: ['/og-image.png'],
  },
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        {children}
      </body>
    </html>
  )
} 