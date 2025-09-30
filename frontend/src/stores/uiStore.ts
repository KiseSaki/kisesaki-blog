/**
 * 应用 UI 状态管理
 * 管理主题、侧边栏状态、加载状态等 UI 相关状态
 */

import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface UIState {
  // 侧边栏状态
  sidebar: {
    collapsed: boolean;
    width: number;
    collapsedWidth: number;
  };

  // 全局加载状态
  loading: {
    global: boolean;
    requests: Set<string>;
  };

  // 模态框状态
  modal: {
    visible: boolean;
    type?: string;
    data?: unknown;
  };
}

interface UIActions {
  // 侧边栏相关操作
  toggleSidebar: () => void;
  setSidebarCollapsed: (collapsed: boolean) => void;

  // 加载状态操作
  setGlobalLoading: (loading: boolean) => void;
  addRequest: (requestId: string) => void;
  removeRequest: (requestId: string) => void;

  // 模态框操作
  showModal: (type: string, data?: unknown) => void;
  hideModal: () => void;

  // 重置所有状态
  resetUI: () => void;
}

type UIStore = UIState & UIActions;

const initialState: UIState = {
  sidebar: {
    collapsed: false,
    width: 192, // w-48 = 12rem = 192px
    collapsedWidth: 96, // w-24 = 6rem = 96px
  },
  loading: {
    global: false,
    requests: new Set(),
  },
  modal: {
    visible: false,
    type: undefined,
    data: undefined,
  },
};

/**
 * UI 状态管理 Store
 * 持久化存储侧边栏状态等用户首选项
 */
export const useUIStore = create<UIStore>()(
  persist(
    (set, get) => ({
      ...initialState,

      // 侧边栏操作
      toggleSidebar: () => {
        set(state => ({
          sidebar: {
            ...state.sidebar,
            collapsed: !state.sidebar.collapsed,
          },
        }));
      },

      setSidebarCollapsed: (collapsed: boolean) => {
        set(state => ({
          sidebar: {
            ...state.sidebar,
            collapsed,
          },
        }));
      },

      // 全局加载状态
      setGlobalLoading: (loading: boolean) => {
        set(state => ({
          loading: {
            ...state.loading,
            global: loading,
          },
        }));
      },

      // 请求加载状态管理
      addRequest: (requestId: string) => {
        const { requests } = get().loading;
        const newRequests = new Set(requests);
        newRequests.add(requestId);
        set(state => ({
          loading: {
            ...state.loading,
            requests: newRequests,
          },
        }));
      },

      removeRequest: (requestId: string) => {
        const { requests } = get().loading;
        const newRequests = new Set(requests);
        newRequests.delete(requestId);
        set(state => ({
          loading: {
            ...state.loading,
            requests: newRequests,
          },
        }));
      },

      // 模态框操作
      showModal: (type: string, data?: unknown) => {
        set({
          modal: {
            visible: true,
            type,
            data,
          },
        });
      },

      hideModal: () => {
        set({
          modal: {
            visible: false,
            type: undefined,
            data: undefined,
          },
        });
      },

      // 重置所有状态
      resetUI: () => {
        set({
          ...initialState,
          loading: {
            ...initialState.loading,
            requests: new Set(),
          },
        });
      },
    }),
    {
      name: 'ui-storage', // localStorage key
      // 只持久化侧边栏状态，其他状态不需要持久化
      partialize: state => ({
        sidebar: state.sidebar,
      }),
    }
  )
);

/**
 * UI Hook
 * 提供便捷的 UI 状态操作方法
 */
export const useUI = () => {
  const store = useUIStore();

  return {
    // 侧边栏
    sidebarCollapsed: store.sidebar.collapsed,
    sidebarWidth: store.sidebar.width,
    sidebarCollapsedWidth: store.sidebar.collapsedWidth,
    toggleSidebar: store.toggleSidebar,
    setSidebarCollapsed: store.setSidebarCollapsed,

    // 加载状态
    isGlobalLoading: store.loading.global,
    isAnyRequestLoading: store.loading.requests.size > 0,
    setGlobalLoading: store.setGlobalLoading,
    addRequest: store.addRequest,
    removeRequest: store.removeRequest,

    // 模态框
    isModalVisible: store.modal.visible,
    modalType: store.modal.type,
    modalData: store.modal.data,
    showModal: store.showModal,
    hideModal: store.hideModal,

    // 重置
    resetUI: store.resetUI,
  };
};
