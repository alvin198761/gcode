import defaultSettings from '@/settings'

const title = defaultSettings.title || '代码生成器 V3'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
