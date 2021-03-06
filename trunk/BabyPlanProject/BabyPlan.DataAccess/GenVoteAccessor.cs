﻿/**
 * @author yangchao
 * @email:aaronyangchao@gmail.com
 * @date: 2012/6/26 1:18:35
 */
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using System.Collections;
using MySql.Data;
using MySql.Data.MySqlClient;
using BabyPlan.Common;
using System.Collections;
using BabyPlan.Meta;


namespace BabyPlan.DataAccess
{
    public class GenVoteAccessor
    {
        private MySqlCommand cmdInsertGenVote;
        private MySqlCommand cmdDeleteGenVote;
        private MySqlCommand cmdUpdateGenVote;
        private MySqlCommand cmdLoadGenVote;
        private MySqlCommand cmdLoadAllGenVote;
        private MySqlCommand cmdGetGenVoteCount;
        private MySqlCommand cmdGetGenVote;

        private GenVoteAccessor()
        {
            #region cmdInsertGenVote

            cmdInsertGenVote = new MySqlCommand("INSERT INTO gen_vote(vid,vtitle,v_des,obj_id,obj_type,up_num,down_num,create_time,create_id,state) values (@Vid,@Vtitle,@VDes,@ObjId,@ObjType,@UpNum,@DownNum,@CreateTime,@CreateId,@State)");

            cmdInsertGenVote.Parameters.Add("@Vid", MySqlDbType.Int32);
            cmdInsertGenVote.Parameters.Add("@Vtitle", MySqlDbType.String);
            cmdInsertGenVote.Parameters.Add("@VDes", MySqlDbType.String);
            cmdInsertGenVote.Parameters.Add("@ObjId", MySqlDbType.Int32);
            cmdInsertGenVote.Parameters.Add("@ObjType", MySqlDbType.Int32);
            cmdInsertGenVote.Parameters.Add("@UpNum", MySqlDbType.Int32);
            cmdInsertGenVote.Parameters.Add("@DownNum", MySqlDbType.Int32);
            cmdInsertGenVote.Parameters.Add("@CreateTime", MySqlDbType.DateTime);
            cmdInsertGenVote.Parameters.Add("@CreateId", MySqlDbType.Int32);
            cmdInsertGenVote.Parameters.Add("@State", MySqlDbType.Int32);
            #endregion

            #region cmdUpdateGenVote

            cmdUpdateGenVote = new MySqlCommand(" update gen_vote set vid = @Vid,vtitle = @Vtitle,v_des = @VDes,obj_id = @ObjId,obj_type = @ObjType,up_num = @UpNum,down_num = @DownNum,create_time = @CreateTime,create_id = @CreateId,state = @State where vid = @Vid");
            cmdUpdateGenVote.Parameters.Add("@Vid", MySqlDbType.Int32);
            cmdUpdateGenVote.Parameters.Add("@Vtitle", MySqlDbType.String);
            cmdUpdateGenVote.Parameters.Add("@VDes", MySqlDbType.String);
            cmdUpdateGenVote.Parameters.Add("@ObjId", MySqlDbType.Int32);
            cmdUpdateGenVote.Parameters.Add("@ObjType", MySqlDbType.Int32);
            cmdUpdateGenVote.Parameters.Add("@UpNum", MySqlDbType.Int32);
            cmdUpdateGenVote.Parameters.Add("@DownNum", MySqlDbType.Int32);
            cmdUpdateGenVote.Parameters.Add("@CreateTime", MySqlDbType.DateTime);
            cmdUpdateGenVote.Parameters.Add("@CreateId", MySqlDbType.Int32);
            cmdUpdateGenVote.Parameters.Add("@State", MySqlDbType.Int32);

            #endregion

            #region cmdLoadGenVote

            cmdLoadGenVote = new MySqlCommand(@" select vid,vtitle,v_des,obj_id,obj_type,up_num,down_num,create_time,create_id,state from gen_vote limit @PageIndex,@PageSize");
            cmdLoadGenVote.Parameters.Add("@pageIndex", MySqlDbType.Int32);
            cmdLoadGenVote.Parameters.Add("@pageSize", MySqlDbType.Int32);

            #endregion

            #region cmdGetGenVoteCount

            cmdGetGenVoteCount = new MySqlCommand(" select count(*)  from gen_vote ");

            #endregion

            #region cmdLoadAllGenVote

            cmdLoadAllGenVote = new MySqlCommand(" select vid,vtitle,v_des,obj_id,obj_type,up_num,down_num,create_time,create_id,state from gen_vote");

            #endregion

            #region cmdGetGenVote

            cmdGetGenVote = new MySqlCommand(" select vid,vtitle,v_des,obj_id,obj_type,up_num,down_num,create_time,create_id,state from gen_vote where vid = @Vid");
            cmdGetGenVote.Parameters.Add("@Vid", MySqlDbType.Int32);

            #endregion
        }

        /// <summary>
        /// 添加数据
        /// <param name="es">数据实体对象数组</param>
        /// <returns></returns>
        /// </summary>
        public bool Insert(GenVote e)
        {
            MySqlConnection oc = ConnectManager.Create();
            MySqlCommand _cmdInsertGenVote = cmdInsertGenVote.Clone() as MySqlCommand;
            bool returnValue = false;
            _cmdInsertGenVote.Connection = oc;
            try
            {
                if (oc.State == ConnectionState.Closed)
                    oc.Open();
                _cmdInsertGenVote.Parameters["@Vid"].Value = e.Vid;
                _cmdInsertGenVote.Parameters["@Vtitle"].Value = e.Vtitle;
                _cmdInsertGenVote.Parameters["@VDes"].Value = e.VDes;
                _cmdInsertGenVote.Parameters["@ObjId"].Value = e.ObjId;
                _cmdInsertGenVote.Parameters["@ObjType"].Value = e.ObjType;
                _cmdInsertGenVote.Parameters["@UpNum"].Value = e.UpNum;
                _cmdInsertGenVote.Parameters["@DownNum"].Value = e.DownNum;
                _cmdInsertGenVote.Parameters["@CreateTime"].Value = e.CreateTime;
                _cmdInsertGenVote.Parameters["@CreateId"].Value = e.CreateId;
                _cmdInsertGenVote.Parameters["@State"].Value = e.State;

                _cmdInsertGenVote.ExecuteNonQuery();
                return returnValue;
            }
            finally
            {
                oc.Close();
                oc.Dispose();
                oc = null;
                _cmdInsertGenVote.Dispose();
                _cmdInsertGenVote = null;
            }
        }

        /// <summary>
        /// 修改指定的数据
        /// <param name="e">修改后的数据实体对象</param>
        /// <para>数据对应的主键必须在实例中设置</para>
        /// </summary>
        public void Update(GenVote e)
        {
            MySqlConnection oc = ConnectManager.Create();
            MySqlCommand _cmdUpdateGenVote = cmdUpdateGenVote.Clone() as MySqlCommand;
            _cmdUpdateGenVote.Connection = oc;

            try
            {
                if (oc.State == ConnectionState.Closed)
                    oc.Open();

                _cmdUpdateGenVote.Parameters["@Vid"].Value = e.Vid;
                _cmdUpdateGenVote.Parameters["@Vtitle"].Value = e.Vtitle;
                _cmdUpdateGenVote.Parameters["@VDes"].Value = e.VDes;
                _cmdUpdateGenVote.Parameters["@ObjId"].Value = e.ObjId;
                _cmdUpdateGenVote.Parameters["@ObjType"].Value = e.ObjType;
                _cmdUpdateGenVote.Parameters["@UpNum"].Value = e.UpNum;
                _cmdUpdateGenVote.Parameters["@DownNum"].Value = e.DownNum;
                _cmdUpdateGenVote.Parameters["@CreateTime"].Value = e.CreateTime;
                _cmdUpdateGenVote.Parameters["@CreateId"].Value = e.CreateId;
                _cmdUpdateGenVote.Parameters["@State"].Value = e.State;

                _cmdUpdateGenVote.ExecuteNonQuery();

            }
            finally
            {
                oc.Close();
                oc.Dispose();
                oc = null;
                _cmdUpdateGenVote.Dispose();
                _cmdUpdateGenVote = null;
                GC.Collect();
            }
        }

        /// <summary>
        /// 根据条件分页获取指定数据
        /// <param name="pageIndex">当前页</param>
        /// <para>索引从0开始</para>
        /// <param name="pageSize">每页记录条数</param>
        /// <para>记录数必须大于0</para>
        /// </summary>
        public PageEntity<GenVote> Search(Int32 Vid, String Vtitle, String VDes, Int32 ObjId, Int32 ObjType, Int32 UpNum, Int32 DownNum, DateTime CreateTime, Int32 CreateId, Int32 State, int pageIndex, int pageSize)
        {
            PageEntity<GenVote> returnValue = new PageEntity<GenVote>();
            MySqlConnection oc = ConnectManager.Create();
            MySqlCommand _cmdLoadGenVote = cmdLoadGenVote.Clone() as MySqlCommand;
            MySqlCommand _cmdGetGenVoteCount = cmdGetGenVoteCount.Clone() as MySqlCommand;
            _cmdLoadGenVote.Connection = oc;
            _cmdGetGenVoteCount.Connection = oc;

            try
            {
                _cmdLoadGenVote.Parameters["@PageIndex"].Value = pageIndex * pageSize ;
                _cmdLoadGenVote.Parameters["@PageSize"].Value = (pageIndex + 1) * pageSize; 
                _cmdLoadGenVote.Parameters["@Vid"].Value = Vid;
                _cmdLoadGenVote.Parameters["@Vtitle"].Value = Vtitle;
                _cmdLoadGenVote.Parameters["@VDes"].Value = VDes;
                _cmdLoadGenVote.Parameters["@ObjId"].Value = ObjId;
                _cmdLoadGenVote.Parameters["@ObjType"].Value = ObjType;
                _cmdLoadGenVote.Parameters["@UpNum"].Value = UpNum;
                _cmdLoadGenVote.Parameters["@DownNum"].Value = DownNum;
                _cmdLoadGenVote.Parameters["@CreateTime"].Value = CreateTime;
                _cmdLoadGenVote.Parameters["@CreateId"].Value = CreateId;
                _cmdLoadGenVote.Parameters["@State"].Value = State;

                if (oc.State == ConnectionState.Closed)
                    oc.Open();

                MySqlDataReader reader = _cmdLoadGenVote.ExecuteReader();
                while (reader.Read())
                {
                    returnValue.Items.Add(new GenVote().BuildSampleEntity(reader));
                }
                returnValue.RecordsCount = Convert.ToInt32(_cmdGetGenVoteCount.ExecuteScalar());
            }
            finally
            {
                oc.Close();
                oc.Dispose();
                oc = null;
                _cmdLoadGenVote.Dispose();
                _cmdLoadGenVote = null;
                _cmdGetGenVoteCount.Dispose();
                _cmdGetGenVoteCount = null;
                GC.Collect();
            }
            return returnValue;

        }

        /// <summary>
        /// 获取全部数据
        /// </summary>
        public List<GenVote> Search()
        {
            MySqlConnection oc = ConnectManager.Create();
            MySqlCommand _cmdLoadAllGenVote = cmdLoadAllGenVote.Clone() as MySqlCommand;
            _cmdLoadAllGenVote.Connection = oc; List<GenVote> returnValue = new List<GenVote>();
            try
            {
                _cmdLoadAllGenVote.CommandText = string.Format(_cmdLoadAllGenVote.CommandText, string.Empty);
                if (oc.State == ConnectionState.Closed)
                    oc.Open();

                MySqlDataReader reader = _cmdLoadAllGenVote.ExecuteReader();
                while (reader.Read())
                {
                    returnValue.Add(new GenVote().BuildSampleEntity(reader));
                }
            }
            finally
            {
                oc.Close();
                oc.Dispose();
                oc = null;
                _cmdLoadAllGenVote.Dispose();
                _cmdLoadAllGenVote = null;
                GC.Collect();
            }
            return returnValue;
        }

        /// <summary>
        /// 获取指定记录
        /// <param name="id">Id值</param>
        /// </summary>
        public GenVote Get(int Vid)
        {
            GenVote returnValue = null;
            MySqlConnection oc = ConnectManager.Create();
            MySqlCommand _cmdGetGenVote = cmdGetGenVote.Clone() as MySqlCommand;

            _cmdGetGenVote.Connection = oc;
            try
            {
                _cmdGetGenVote.Parameters["@Vid"].Value = Vid;

                if (oc.State == ConnectionState.Closed)
                    oc.Open();

                MySqlDataReader reader = _cmdGetGenVote.ExecuteReader();
                if (reader.HasRows)
                {
                    reader.Read();
                    returnValue = new GenVote().BuildSampleEntity(reader);
                }
            }
            finally
            {
                oc.Close();
                oc.Dispose();
                oc = null;
                _cmdGetGenVote.Dispose();
                _cmdGetGenVote = null;
                GC.Collect();
            }
            return returnValue;

        }
        private static readonly GenVoteAccessor instance = new GenVoteAccessor();
        public static GenVoteAccessor Instance
        {
            get
            {
                return instance;
            }
        }

    }
}
