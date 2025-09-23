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
