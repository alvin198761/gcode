import {post ,postBody} from  '../util/http.js';
/**
 * 查询数据
 */
export const queryTables =()=>{
  return post("/api/code/queryTable");
}
