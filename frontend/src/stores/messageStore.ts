import { create } from "zustand";
import { produce } from "immer";

interface Message {
    id: string;
    content: string;
    sender: string;
    timestamp: number;
    test: {
        nestedField: string;
    }
}

interface MessageStore {
    name: string
    message: Message;
    addMessage: (message: Message) => void;
    removeMessage: (id: string) => void;
    clearMessages: () => void;
    getMessages: () => Message[];
    deepUpdate: (newTestNestedField: string) => void;
    shallowMerge: () => void;
    replace: () => void;
}

const useMessageStore = create<MessageStore>((set, get) => ({
    name: '123123',
    message: {} as Message,
    addMessage: (message: Message) => set({ message }),
    removeMessage: () => set({ message: {} as Message }),
    clearMessages: () => set({ message: {} as Message }),
    getMessages: () => [get().message],
    deepUpdate: (newTestNestedField: string) => set(produce((state: { message: Message }) => {
        const message = state.message;
        if (message) {
            message.test.nestedField = newTestNestedField;
        }
    })),
    shallowMerge: () => {
        const message = get().message;
        if (message) {
            set({ message: { ...message, test: { ...message.test }, id: '123' } });
            console.log(message)
        }
    },
    replace: () => set({}, true)
}));

export default useMessageStore;