# VG Language Main Website

This is the main website for VG Language, accessible at `vglang.com`.

## Features

- Modern, responsive design with dark theme
- Hero section with download and documentation links
- Feature showcase highlighting VG Language capabilities
- Code examples demonstrating syntax
- Cross-domain navigation to docs and packages

## Tech Stack

- **Framework**: Next.js 14 with App Router
- **Styling**: Tailwind CSS
- **Icons**: Lucide React
- **TypeScript**: Full type safety
- **Deployment**: Vercel/Netlify ready

## Getting Started

### Prerequisites

- Node.js 18+ installed
- npm or yarn package manager

### Installation

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Start production server
npm start
```

### Environment Variables

Create a `.env.local` file:

```env
NEXT_PUBLIC_SITE_URL=https://vglang.com
NEXT_PUBLIC_DOCS_URL=https://docs.vglang.com
NEXT_PUBLIC_PACKAGES_URL=https://packages.vglang.com
```

## Project Structure

```
vg-website/
├── src/
│   ├── app/
│   │   ├── globals.css
│   │   ├── layout.tsx
│   │   └── page.tsx
│   └── components/ (future components)
├── public/ (static assets)
├── tailwind.config.ts
├── next.config.js
├── package.json
└── tsconfig.json
```

## Customization

### Colors

The color scheme is defined in `tailwind.config.ts`:

- **Primary**: Amber/yellow theme (`primary-400`, `primary-500`, etc.)
- **Dark**: Dark theme colors (`dark-800`, `dark-900`, etc.)

### Content

Update the homepage content in `src/app/page.tsx`:

- Hero section text and links
- Feature descriptions
- Code examples
- Footer links

## Deployment

### Vercel (Recommended)

1. Connect your GitHub repository to Vercel
2. Set build directory to `vg-website`
3. Configure custom domain: `vglang.com`
4. Set environment variables

### Netlify

1. Connect repository to Netlify
2. Set build directory to `vg-website`
3. Set build command to `npm run build`
4. Configure custom domain: `vglang.com`

## Cross-Domain Links

The website includes links to:

- **Documentation**: `https://docs.vglang.com`
- **Packages**: `https://packages.vglang.com`
- **GitHub**: `https://github.com/Husseinabdulameer11/vg-lang`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test locally with `npm run dev`
5. Submit a pull request

## License

This project is part of the VG Language ecosystem. 