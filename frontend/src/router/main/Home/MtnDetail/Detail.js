import { fontFamily } from "@mui/system";
import { useParams } from "react-router-dom";
import axios from "axios";
import React, { useEffect, useState } from "react";
import back from "../../../../res/img/back.png";
import check from "../../../../res/img/check.png";
import checked from "../../../../res/img/checked.png";
import Bar from "../Bar";
import Star from "./Star";
import About from "./About";
import Home from "./Home";
import { connect } from "react-redux";
import Send from "../../../../config/Send";
function Detail({ userSlice }) {
  const [isVisited, setIsVisited] = useState(false);
  const [tab, setTab] = useState("home");
  const [Info, setInfo] = useState([]);
  const [visiter, setVisiter] = useState(0);
  const [distance, setDistance] = useState(0);
  const [count, setCount] = useState(0);
  let useParam = useParams();
  const url = "https://k6a306.p.ssafy.io/api";

  useEffect(() => {
    getMtnInfo();
    // getTotal();
  }, []);

  function getMtnInfo() {
    Send.get(url + `/mountain/${useParam.mountainCode}`)
      .then((res) => {
        if (res.status === 200) {
          console.log(res);
          setInfo(res.data.mountainInfo);
          window.localStorage.setItem("mountainId", res.data.mountainInfo.id);
          getTotal();
        }
      })
      .catch((e) => {
        console.log(e);
      });
  }

  function getTotal() {
    axios
      .get(url + `/plogging`, {
        params: {
          mountainCode: useParam.mountainCode,
        },
      })
      .then(function (response) {
        console.log(response.data);
        setVisiter(response.data.visiter);
        setDistance(response.data.distance);
        setCount(response.data.count);
      })
      .catch(function (error) {
        console.log(error);
      });
  }
  return (
    <>
      <div style={{ height: "92.5vh" }}>
        <div
          id="header"
          style={{ marginLeft: "6vw", marginRight: "6vw", height: "9vh" }}
        >
          <div>
            <img
              style={{
                marginTop: "4vh",
                width: "5vw",
                height: "5vw",
              }}
              src={back}
            />
          </div>
        </div>

        <div
          id="mountainName"
          style={{
            marginLeft: "6vw",
            marginRight: "6vw",
            height: "5vh",
            display: "flex",
          }}
        >
          <div
            style={{
              marginLeft: "3vw",
              fontSize: "3.5vh",
              fontWeight: "500",
            }}
          >
            {Info.name}
          </div>

          <div>
            {count == 0 ? (
              <img
                style={{
                  marginLeft: "2vw",
                  marginTop: "1.5vh",
                  width: "6vw",
                  height: "6vw",
                }}
                src={check}
              />
            ) : (
              <div style={{ color: "#69696C" }}>
                <img
                  style={{
                    marginLeft: "2vw",
                    marginTop: "1.5vh",
                    width: "6vw",
                    height: "6vw",
                    marginRight: "1vw",
                  }}
                  src={checked}
                />
                <div>{count}회 방문</div>
              </div>
            )}
          </div>
        </div>

        <div
          id="address"
          style={{
            marginLeft: "9vw",
            marginRight: "6vw",
            marginBottom: "2.2vh",
            fontSize: "1.8vh",
            color: "#69696C",
          }}
        >
          {Info.address}
        </div>

        <div id="tabs" style={{ height: "74vh" }}>
          {tab == "star" ? (
            <>
              <div
                id="tab"
                style={{
                  marginLeft: "9vw",
                  marginRight: "6vw",
                  fontSize: "2vh",
                  display: "flex",
                  marginBottom: "2vh",
                }}
              >
                <div
                  style={{ marginRight: "2.5vw" }}
                  onClick={() => setTab("home")}
                >
                  Home
                </div>
                <div
                  style={{
                    fontWeight: 900,
                    color: "#002831",
                    marginRight: "2.5vw",
                  }}
                >
                  명예의 전당
                </div>
                <div onClick={() => setTab("about")}>About {Info.name}</div>
              </div>
              <Star mountainCode={useParam.mountainCode} url={url}></Star>
            </>
          ) : tab == "about" ? (
            <>
              <div
                id="tab"
                style={{
                  marginLeft: "9vw",
                  marginRight: "6vw",
                  fontSize: "2vh",
                  display: "flex",
                  marginBottom: "2vh",
                }}
              >
                <div
                  style={{ marginRight: "2.5vw" }}
                  onClick={() => setTab("home")}
                >
                  Home
                </div>
                <div
                  onClick={() => setTab("star")}
                  style={{ marginRight: "2.5vw" }}
                >
                  명예의 전당
                </div>
                <div
                  style={{
                    fontWeight: 900,
                    color: "#002831",
                  }}
                >
                  About {Info.name}
                </div>
              </div>
              <About url={url}></About>
            </>
          ) : (
            <>
              <div
                id="tab"
                style={{
                  marginLeft: "9vw",
                  marginRight: "6vw",
                  fontSize: "2vh",
                  display: "flex",
                  marginBottom: "1.5vh",
                }}
              >
                <div
                  style={{
                    fontWeight: 900,
                    color: "#002831",
                    marginRight: "2.5vw",
                  }}
                >
                  Home
                </div>
                <div
                  onClick={() => setTab("star")}
                  style={{ marginRight: "2.5vw" }}
                >
                  명예의 전당
                </div>
                <div onClick={() => setTab("about")}>About {Info.name}</div>
              </div>

              <Home
                visiter={visiter}
                distance={distance}
                lat={Info.lat}
                lng={Info.lng}
              ></Home>
            </>
          )}
        </div>
      </div>
      <Bar></Bar>
    </>
  );
}
function mapStateToProps(state) {
  return { userSlice: state.user };
}
export default connect(mapStateToProps)(Detail);
