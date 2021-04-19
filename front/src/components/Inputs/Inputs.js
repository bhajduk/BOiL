import React, { useState, useEffect } from "react";
import Axios from "axios";
import "./Inputs.css";
import axios from "axios";

const Inputs = () => {
    const [postId, setPostId] = useState(null);
    const [submit, setSubmit] = useState(false);

    const [transportCostsTable, setTransportCostsTable] = useState(null);
    const [supplyTable, setSupplyTable] = useState(null);
    const [demandTable, setDemandTable] = useState(null);

    const [demand1, setDemand1] = useState(null);
    const [demand2, setDemand2] = useState(null);

    const handleDemand1 = (event) => {
        setDemand1(event.target.value);
    };

    const handleDemand2 = (event) => {
        setDemand2(event.target.value);
    };

    const [supply1, setSupply1] = useState(null);
    const [supply2, setSupply2] = useState(null);
    const [supply3, setSupply3] = useState(null);

    const handleSupply1 = (event) => {
        setSupply1(event.target.value);
    };

    const handleSupply2 = (event) => {
        setSupply2(event.target.value);
    };

    const handleSupply3 = (event) => {
        setSupply3(event.target.value);
    };

    const [cost11, setCost11] = useState(null);
    const [cost12, setCost12] = useState(null);
    const [cost21, setCost21] = useState(null);
    const [cost22, setCost22] = useState(null);
    const [cost31, setCost31] = useState(null);
    const [cost32, setCost32] = useState(null);

    const handleCost11 = (event) => {
        setCost11(event.target.value);
    };
    const handleCost12 = (event) => {
        setCost12(event.target.value);
    };
    const handleCost21 = (event) => {
        setCost21(event.target.value);
    };
    const handleCost22 = (event) => {
        setCost22(event.target.value);
    };
    const handleCost31 = (event) => {
        setCost31(event.target.value);
    };
    const handleCost32 = (event) => {
        setCost32(event.target.value);
    };

    let receivers = 2;

    const url = "http://localhost:2137/broker/solve";

    const axSubmit = () => {
        axios({
            method: "post",
            url: url,
            data: {
                transportCostsTable: transportCostsTable,
                supplyTable: supplyTable,
                demandTable: demandTable,
                lockedRoute: parseFloat(-1),
            },
        }).then((res) => console.log(res.data));
    };

    return (
        <div>
            <div
                className="inputsContainer"
                style={{
                    gridTemplateColumns: `repeat(${receivers + 1}, auto)`,
                }}
            >
                <div></div>
                <div>
                    <input
                        type="number"
                        placeholder="Demand 1"
                        onChange={handleDemand1}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Demand 2"
                        onChange={handleDemand2}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Supply 1"
                        onChange={handleSupply1}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Transport cost 1-1"
                        onChange={handleCost11}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Transport cost 1-2"
                        onChange={handleCost12}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Supply 2"
                        onChange={handleSupply2}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Transport cost 2-1"
                        onChange={handleCost21}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Transport cost 2-2"
                        onChange={handleCost22}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Supply 3"
                        onChange={handleSupply3}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Transport cost 3-1"
                        onChange={handleCost31}
                    />
                </div>
                <div>
                    <input
                        type="number"
                        placeholder="Transport cost 3-2"
                        onChange={handleCost32}
                    />
                </div>
                <div></div>
                <div>
                    <div
                        className="btn"
                        onClick={() => {
                            console.log(
                                demand1,
                                demand2,
                                supply1,
                                cost11,
                                cost12,
                                supply2,
                                cost21,
                                cost22,
                                supply3,
                                cost31,
                                cost32
                            );

                            setTransportCostsTable([
                                [parseFloat(cost11), parseFloat(cost12)],
                                [parseFloat(cost21), parseFloat(cost22)],
                                [parseFloat(cost31), parseFloat(cost32)],
                            ]);

                            setSupplyTable([
                                parseFloat(supply1),
                                parseFloat(supply2),
                                parseFloat(supply3),
                            ]);

                            setDemandTable([
                                parseFloat(demand1),
                                parseFloat(demand2),
                            ]);

                            setSubmit(true);
                            axSubmit();
                        }}
                    >
                        <p className="noselect">submit</p>
                    </div>
                </div>
                <div></div>
            </div>
        </div>
    );
};

export default Inputs;
