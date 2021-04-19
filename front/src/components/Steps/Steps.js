import React, { useState } from "react";
import "./Steps.css";

const Steps = () => {
    const step = [
        [
            [10, 19],
            [2, 14],
            [6, 8],
            [11, 9],
        ],
        [
            [2, 3],
            [7, 1],
            [4, 12],
        ],
        [
            [9, 7],
            [5, 11],
            [3, 2],
        ],
        [
            [1, 2],
            [3, 44],
            [333, 111],
        ],
    ];

    const [data, setData] = useState([]);
    const [counter, setCounter] = useState(0);

    const nextStep = () => {
        setData(step[counter]);
        if (counter === step.length - 1) {
            setCounter(0);
        } else setCounter(counter + 1);
    };

    return (
        <div>
            <div className="steps">
                {data.map((elem) => {
                    return elem.map((el) => {
                        return <h3>{el}</h3>;
                    });
                })}
            </div>
            <div className="stepBtn noselect" onClick={nextStep}>
                {data.length > 1 ? <p>next step</p> : <p>show steps</p>}
            </div>
        </div>
    );
};

export default Steps;

// return (
//     <div className="steps">
//         {step.map((elem) => {
//             return elem.map((el) => {
//                 return <h1>{el}</h1>;
//             });
//         })}
//     </div>
// );

//////////to poniżej działa dla 3d
// return (
//     <div className="steps">
//         {step.map((elem) => {
//             return elem.map((el) => {
//                 return el.map((e) => {
//                     return <h1>{e}</h1>;
//                 });
//             });
//         })}
//     </div>
// );
