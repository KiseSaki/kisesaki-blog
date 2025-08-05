import useMessageStore from "@/stores/messageStore";
import { NavLink } from "react-router";
import {useShallow} from 'zustand/react/shallow'

const Home = () => {
    const {message, name} = useMessageStore(
        useShallow((state) => ({
            message: state.message,
            name: state.name,
        }))
    )

    const shallowMerge = useMessageStore((state) => state.shallowMerge);
    const replace = useMessageStore((state) => state.replace);
    return (
        <div>
            <h1>Welcome to the Home Page</h1>
            <NavLink to="/about">Go to About Page</NavLink>
            <div>{name}</div>
            <ul>
                {message && Object.keys(message).length > 0 ? (
                    <li>
                        <strong>{JSON.stringify(message)}</strong>
                    </li>
                ) : (
                    <li>No message available</li>
                )}
            </ul>

            <button className="p-2 bg-blue-500 text-white rounded" onClick={() => {
                shallowMerge();
            }}>
                Shallow Merge
            </button>

            <button className="p-2 bg-blue-500 text-white rounded" onClick={() => {
                replace();
            }}>
                Replace
            </button>
        </div>
    );
}

export default Home;