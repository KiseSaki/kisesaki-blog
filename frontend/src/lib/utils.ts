import { clsx, type ClassValue } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * 一个预定义的、视觉上比较舒服的背景色板
 */
const PALETTE = [
  'bg-red-200',
  'bg-orange-200',
  'bg-amber-200',
  'bg-yellow-200',
  'bg-lime-200',
  'bg-green-200',
  'bg-emerald-200',
  'bg-teal-200',
  'bg-cyan-200',
  'bg-sky-200',
  'bg-blue-200',
  'bg-indigo-200',
  'bg-violet-200',
  'bg-purple-200',
  'bg-fuchsia-200',
  'bg-pink-200',
  'bg-rose-200',
];

/**
 * 根据输入字符串，从预定义的色板中确定性地选择一个颜色类名。
 * @param str 用来生成颜色的唯一标识符，比如用户名、ID等。
 * @returns 返回一个 Tailwind CSS 的背景色类名。
 */
export function getDeterministicColor(str: string): string {
  // 如果字符串为空，返回一个默认颜色
  if (!str || str.length === 0) {
    return 'bg-gray-200';
  }

  // 一个简单的哈希算法：将字符串中每个字符的 charCode 相加
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash += str.charCodeAt(i);
  }

  // 使用取模运算，确保索引值在 PALETTE 数组的有效范围内
  const index = hash % PALETTE.length;
  return PALETTE[index];
}

/**
 * 将日期格式化为相对时间字符串，例如：刚刚 / 3分钟前 / 2小时前 / 1天2小时3分钟前 / 4月前 / 2年前
 * 默认规则：
 * - 小于 1 分钟 => 刚刚
 * - 小于 1 小时 => N 分钟前
 * - 小于 1 天 => H 小时 M 分钟前
 * - 小于 30 天 => D 天 H 小时 M 分钟前 (天数优先)
 * - 小于 12 个月 => N 月前
 * - 其余 => N 年前
 *
 * @param date 要格式化的日期（Date 实例或可被 new Date() 解析的字符串/数字）
 * @param options 可选项：{
 *   maxUnit?: 'year' | 'month' | 'day' | 'hour' | 'minute'  // 控制最大显示单位
 * }
 */
export function formatTimeAgo(
  date: Date | string | number,
  options?: { maxUnit?: 'year' | 'month' | 'day' | 'hour' | 'minute' }
): string {
  const now = new Date();
  const then = date instanceof Date ? date : new Date(date);
  if (isNaN(then.getTime())) return '';

  const diffMs = now.getTime() - then.getTime();
  const minute = 60 * 1000;
  const hour = 60 * minute;
  const day = 24 * hour;
  const month = 30 * day; // 近似
  const year = 365 * day; // 近似

  if (diffMs < minute) return '刚刚';

  const years = Math.floor(diffMs / year);
  const months = Math.floor(diffMs / month);
  const days = Math.floor(diffMs / day);
  const hours = Math.floor((diffMs % day) / hour);
  const minutes = Math.floor((diffMs % hour) / minute);

  const maxUnit = options?.maxUnit || 'year';

  if (
    years >= 1 &&
    (maxUnit === 'year' ||
      maxUnit === 'month' ||
      maxUnit === 'day' ||
      maxUnit === 'hour' ||
      maxUnit === 'minute')
  ) {
    return `${years}年前`;
  }

  if (
    months >= 1 &&
    (maxUnit === 'month' ||
      maxUnit === 'day' ||
      maxUnit === 'hour' ||
      maxUnit === 'minute')
  ) {
    return `${months}月前`;
  }

  if (
    days >= 1 &&
    (maxUnit === 'day' || maxUnit === 'hour' || maxUnit === 'minute')
  ) {
    // 显示 天 + 时 + 分（如果为 0 则省略）
    const parts: string[] = [];
    parts.push(`${days}天`);
    if (hours > 0) parts.push(`${hours}时`);
    if (minutes > 0) parts.push(`${minutes}分`);
    return parts.join('') + '前';
  }

  if (hours >= 1 && (maxUnit === 'hour' || maxUnit === 'minute')) {
    // 显示 小时 + 分钟
    if (minutes > 0) return `${hours}时${minutes}分钟前`;
    return `${hours}小时前`;
  }

  // fallback: minutes
  return `${minutes}分钟前`;
}
