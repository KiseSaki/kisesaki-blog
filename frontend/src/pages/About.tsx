import useMessageStore from "@/stores/messageStore";
import { NavLink } from "react-router";

const About = () => {
    const addMessage = useMessageStore(state => state.addMessage);
    const deepUpdate = useMessageStore(state => state.deepUpdate);
    return (
        <div>
            <h1>About Us</h1>
            <NavLink to="/">Go back to Home Page</NavLink>
            <br />
            <button className=" p-2 bg-blue-500 text-white rounded"
            onClick={() => {
                addMessage({
                    id: Date.now().toString(),
                    content: "New message from About",
                    sender: "About",
                    timestamp: Date.now(),
                    test: {
                        nestedField: "Initial value"
                    }
                });
            }}
                >
                Add Message
            </button>
            <br />
            <button className=" p-2 bg-blue-500 text-white rounded" onClick={() => {
                deepUpdate("Updated value");
            }}>
                Deep Update
            </button>
        </div>
    );
}

export default About;