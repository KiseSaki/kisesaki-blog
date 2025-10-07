import EmojiPicker, { Theme } from 'emoji-picker-react';
import { useEffect, useRef } from 'react';
import type { EmojiPickerProps } from './types';

/**
 * Emoji 选择器组件
 */
export const EmojiPickerComponent = ({
  onEmojiClick,
  onClose,
}: EmojiPickerProps) => {
  const pickerRef = useRef<HTMLDivElement>(null);

  // 点击外部关闭
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        pickerRef.current &&
        !pickerRef.current.contains(event.target as Node)
      ) {
        onClose();
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [onClose]);

  return (
    <div
      ref={pickerRef}
      className='absolute bottom-full left-0 z-50 mb-2 shadow-xl'
    >
      <EmojiPicker
        onEmojiClick={emojiData => onEmojiClick(emojiData.emoji)}
        theme={Theme.LIGHT}
        width={350}
        height={400}
        searchPlaceHolder='搜索表情...'
        previewConfig={{ showPreview: false }}
      />
    </div>
  );
};
