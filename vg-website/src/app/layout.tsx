import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'VG Language - Simple, Expressive Programming',
  description: 'VG Language is a lightweight, expressive programming language designed for simplicity and ease of use.',
  keywords: ['programming language', 'vg language', 'simple syntax', 'beginner friendly'],
  authors: [{ name: 'VG Language Team' }],
  openGraph: {
    title: 'VG Language - Simple, Expressive Programming',
    description: 'VG Language is a lightweight, expressive programming language designed for simplicity and ease of use.',
    url: 'https://vglang.com',
    siteName: 'VG Language',
    images: [
      {
        url: '/og-image.png',
        width: 1200,
        height: 630,
        alt: 'VG Language',
      },
    ],
    locale: 'en_US',
    type: 'website',
  },
  twitter: {
    card: 'summary_large_image',
    title: 'VG Language - Simple, Expressive Programming',
    description: 'VG Language is a lightweight, expressive programming language designed for simplicity and ease of use.',
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