package com.ddj.commonkit.android.system;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.ddj.commonkit.NumberUtil;

public class RunScript {

	/**
	 * 执行一个shell命令，并返回字符串值
	 * 
	 * @param cmd
	 *            命令名称&参数组成的数组（例如：{"/system/bin/cat", "/proc/version"}）
	 * @param workdirectory
	 *            命令执行路径（例如："system/bin/"）
	 * @return 执行结果组成的字符串
	 * @throws IOException
	 */
	public synchronized String getCommandResult(String[] cmd, String workdirectory) {
		StringBuffer result = new StringBuffer();
		try {
			// 创建操作系统进程（也可以由Runtime.exec()启动）
			// Runtime runtime = Runtime.getRuntime();
			// Process proc = runtime.exec(cmd);
			// InputStream inputstream = proc.getInputStream();
			ProcessBuilder builder = new ProcessBuilder(cmd);

			InputStream in = null;
			// 设置一个路径（绝对路径了就不一定需要）
			if (workdirectory != null) {
				// 设置工作目录（同上）
				builder.directory(new File(workdirectory));
				// 合并标准错误和标准输出
				builder.redirectErrorStream(true);
				// 启动一个新进程
				Process process = builder.start();

				// 读取进程标准输出流
				in = process.getInputStream();
				byte[] re = new byte[1024];
				while (in.read(re) != -1) {
					result = result.append(new String(re));
				}
			}
			// 关闭输入流
			if (in != null) {
				in.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 获取Ps的结果 封装成PsRow对象
	 * 
	 * @author dingdj Date:2014-6-12上午11:57:43
	 * @return
	 */
	public static List<PsRow> getPsResult() {
		List<PsRow> pslist = new ArrayList<PsRow>();
		String result = new RunScript().getCommandResult(new String[] { "ps" }, "/");
		String[] lines = result.split("\n");
		for (String line : lines) {
			PsRow row = new PsRow(line);
			if (row.getPid() != -1) {
				pslist.add(row);
			}
		}
		return pslist;
	}

	public static class PsRow {
		private int rootpid = -1;
		private String user;
		private int pid = -1;
		private String ppid;
		private int mem;
		private String cmd;

		public PsRow(String line) {
			if (line == null)
				return;
			String[] p = line.split("[\\s]+");
			if (p.length != 9)
				return;
			user = p[0];
			pid = Integer.parseInt(p[1]);
			ppid = p[2];
			cmd = p[8];
			mem = NumberUtil.parseInt(p[4], 0);
			if (isRoot()) {
				rootpid = pid;
			}
		}

		public String getCmd() {
			return cmd;
		}

		public int getMem() {
			return mem;
		}

		public int getPid() {
			return pid;
		}

		public String getPpid() {
			return ppid;
		}

		public int getRootpid() {
			return rootpid;
		}

		public String getUser() {
			return user;
		}

		public boolean isMain() {
			return ppid.equals(rootpid) && user.startsWith("app_");
		}

		public boolean isRoot() {
			return "zygote".equals(cmd);
		}

		public void setCmd(String cmd) {
			this.cmd = cmd;
		}

		public void setMem(int mem) {
			this.mem = mem;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public void setPpid(String ppid) {
			this.ppid = ppid;
		}

		public void setRootpid(int rootpid) {
			this.rootpid = rootpid;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String toString() {
			final String TAB = ";";
			String retValue = "";
			retValue = "PsRow ( " + super.toString() + TAB + "pid = " + this.pid + TAB + "cmd = " + this.cmd + TAB + "ppid = " + this.ppid + TAB
					+ "user = " + this.user + TAB + "mem = " + this.mem + " )";

			return retValue;
		}

	}

}