import React, { useCallback, useEffect, useState } from "react";
import Bar from "../Bar";
import "../Home.css";
import Send from "../../../../config/Send";
function ArticleDetail() {
  const [nickname, setNickname] = useState();
  const [title, setTitle] = useState();
  const [description, setDescription] = useState();
  const [createTime, setCreateTime] = useState("2022-05-06T13:20:33.548201");
  const [comments, setComments] = useState([]);

  const [commentContent, setCommentContent] = useState();
  useEffect(() => {
    getArticle();
  }, []);

  function getArticle() {
    const communityId = window.localStorage.getItem("communityId");
    Send.get(`/community/${communityId}`)
      .then((res) => {
        console.log(res);
        setNickname(res.data.nickname);
        setTitle(res.data.title);
        setDescription(res.data.description);
        setCreateTime(res.data.createTime);
        setComments(res.data.comments);
      })
      .catch((e) => {
        console.log(e);
      });
  }

  function createComment() {
    const communityId = window.localStorage.getItem("communityId");
    const data = {
      communityId: communityId,
      description: commentContent,
    };
    Send.post(`/comment`, JSON.stringify(data))
      .then((res) => {
        console.log(res);
        if (res.code === 201) {
          getArticle();
        }
      })
      .catch((e) => {
        console.log(e);
      });
  }
  const onCommentHandler = (event) => {
    setCommentContent(event.currentTarget.value);
  };
  return (
    <>
      {/* 92.5vh */}
      <div
        style={{
          marginLeft: "8.3vw",
          width: "83.3vw",
          marginRight: "8.3vw",
          height: "84vh",
          marginTop: "8.5vh",
        }}
      >
        <div
          id="header"
          style={{
            position: "relative",
            height: "3.5vh",
            fontSize: "1.8vh",
            marginBottom: "3.5vh",
          }}
        >
          <div style={{ fontWeight: "bolder" }}>{nickname}</div>
          <div style={{ color: "#ACACAC", display: "flex" }}>
            <div>
              {createTime.substr(0, 10) + " " + createTime.substr(11, 8)}{" "}
            </div>
          </div>
        </div>
        <div
          id="contents"
          style={{
            position: "relative",
            height: "30vh",
            marginBottom: "2vh",
          }}
        >
          <div
            id="title"
            style={{
              fontSize: "2.2vh",
              fontWeight: "bold",
              marginBottom: "1.5vh",
            }}
          >
            {title}
          </div>
          <div id="content" style={{ fontSize: "1.8vh" }}>
            {description}
          </div>
        </div>
        <div id="comments">
          {comments == undefined ? (
            <div
              style={{
                position: "relative",
                fontWeight: "regular",
                fontSize: "2vh",
                marginBottom: "1.5vh",
              }}
            >
              댓글 0개
            </div>
          ) : (
            <div
              style={{
                position: "relative",
                fontWeight: "regular",
                fontSize: "2vh",
                marginBottom: "1.5vh",
              }}
            >
              댓글 {comments.length}개
            </div>
          )}

          <div
            id="comment"
            className="box"
            style={{ height: "30vh", overflow: "auto" }}
          >
            {comments &&
              comments.map((comment) => (
                //   10.5vh
                <div
                  style={{
                    fontSize: "1.7vh",
                    borderBottom: "1px solid #8E8E92",
                  }}
                >
                  <div style={{ fontWeight: "bold", marginTop: "1.5vh" }}>
                    익명숲지기
                  </div>
                  <div style={{ fontSize: "1.7vh" }}>{comment.description}</div>
                  <div style={{ color: "#ACACAC", marginBottom: "1.5vh" }}>
                    {comment.createTime}
                  </div>
                </div>
              ))}
          </div>
        </div>
        <div>
          <div>
            <input
              className="input"
              onChange={onCommentHandler}
              style={{
                position: "relative",
                background: "#EFEFEF",
                border: "none",
                width: "70.3vw",
                height: "4vh",
                marginTop: "2.7vh",
                paddingLeft: "3vw",
                fontSize: "1.8vh",
                paddingRight: "10vw",
              }}
              placeholder="댓글을 작성해보세요"
            ></input>
          </div>
          <div>
            <button
              onClick={() => createComment()}
              style={{
                position: "absolute",
                top: "85vh",
                right: "10vw",
                border: "none",
                background: "none",
                color: "#69696C",
                fontSize: "1.8vh",
              }}
            >
              작성
            </button>
          </div>
        </div>
      </div>
      <Bar></Bar>
    </>
  );
}
export default ArticleDetail;