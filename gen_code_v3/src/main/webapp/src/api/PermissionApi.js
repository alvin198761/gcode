import {post ,postBody} from  '../util/http.js';
/**
 * 查询数据
 */
export const getPermission =(url)=>{
  return post("/api/code/queryTable",{url:url});
}
